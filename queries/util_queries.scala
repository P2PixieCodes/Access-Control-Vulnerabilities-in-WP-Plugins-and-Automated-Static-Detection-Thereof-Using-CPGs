import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks.{break,breakable}

import io.shiftleft.codepropertygraph.generated.*
import io.shiftleft.codepropertygraph.generated.nodes.*
import io.shiftleft.codepropertygraph.generated.help.*
import io.shiftleft.codepropertygraph.generated.language.*
import io.shiftleft.codepropertygraph.generated.accessors.*
import io.shiftleft.semanticcpg.*
import io.shiftleft.semanticcpg.language.*
import flatgraph.traversal.*


def get_calls_for_methods(cpg: Cpg, methods: Iterator[Method])(implicit callResolver: ICallResolver): Iterator[? <: AstNode] = {
    val method_nodes = methods.toSet
       get_inclusion_calls(cpg, method_nodes) // global file functions
    ++ get_calls_via_variable_assignment(cpg, method_nodes) // anonymous functions
    ++ get_calls_via_callbacks(cpg, method_nodes) // regular functions
    ++ method_nodes.callIn // regular functions
    ++ get_object_method_calls(cpg, method_nodes) // attempt to find unresolved function calls
    ++ get_self_construct_call(cpg, method_nodes) // attempt to find calls to __construct functions via references to self
}

def get_self_construct_call(cpg: Cpg, methods: Iterator[Method]): Iterator[Call] = {
    // starting with a "__construct" method, navigate to the containing TYPE_DECL and search within its static methods for a call to `self.__construct`
    methods.name("__construct").isExternal(false).astParentType("TYPE_DECL").flatMap(start_node => 
        start_node.astParent.asInstanceOf[TypeDecl].flatMap(node => 
            cpg.typeDecl.name(node.name + "<metaclass>")
        ).astOut.isMethod.ast.isCall.methodFullName("self.__construct")
    )
}

def get_object_method_calls(cpg: Cpg, methods: Iterator[Method]): Iterator[Call] = {
    /* // PRINT STUFF
    val method_set_print = methods.toSet
    println("\nMETHODS")
    method_set_print.foreach(node => println(s"    ${node.fullName}\n    ---- ${node.code}"))
    println("\nCALLS MATCHING FIRST CONDITION")
    cpg.call.filter(call => method_set_print.exists(method => call.code.contains(s"->${method.name}"))).foreach(call => println(s"    `${call.code}` calls ${call.methodFullName} in line ${call.lineNumber}\n    ---- in method: ${call.method.fullName}"))
    println("\nCALLS MATCHING SECOND CONDITION")
    cpg.call.filter(call => method_set_print.exists(method => call.code.contains(s"->${method.name}") && call.methodFullName.endsWith(method.name))).foreach(call => println(s"    `${call.code}` calls ${call.methodFullName} in line ${call.lineNumber}\n    ---- in method: ${call.method.fullName}"))
    */
    val method_set = methods.toSet
    cpg.call.filter(call => method_set.exists(method => call.code.contains(s"->${method.name}") && call.methodFullName.endsWith(method.name)))
}

/**
  * Given a file <global> method, get all includes/requires of the corresponding file.
  *
  * @param cpg
  *     the CPG instance
  * @param methods
  *     an `Iterator[Method]` which may or may not contain `filename.php:<global>` methods
  * @return 
  *     an `Iterator[Call]` of `include`/`require` statements corresponding to any `<global>` methods; 
  *     if there are no `<global>` methods, the iterator is empty
  */
def get_inclusion_calls(cpg: Cpg, methods: Iterator[Method]): Iterator[Call] = {

    // we assume that files' global methods are named `filename.php:<global>`
    val file_methods = methods.filter(_.name.contains("<global>")).l // fsr this has to be a list - if it's an iterator, it doesn't want to work?
    
    val all_file_calls = cpg.call.or(_.name("include"),_.name("include_once"),_.name("require"),_.name("require_once")).l

    // we assume that the entire filename is passed as a string literal to the statement
    def relevant_file_calls = all_file_calls.iterator.filter(node => file_methods.iterator.exists(method_node => { 
        node.code.contains(method_node.filename)
        || node.code.contains(method_node.filename.substring(method_node.filename.lastIndexOf("/")+1)) // case where the filename without path is passed
    }))

    // TODO: case where the filename/path is dynamically constructed
    
    relevant_file_calls
}

/**
  * Given a filename, get the file's `<global>` method.
  *
  * @param cpg
  *     the CPG instance
  * @param file_name
  *     the given filename as a `String`
  * @return
  *     an `Iterator[Method]` with the corresponding `<global>` file method;
  *     if there is no file with that name, the iterator is empty
  */
def get_entry_method(cpg: Cpg, file_name: String): Iterator[Method] = {
    cpg.file.name(file_name).namespaceBlock.typeDecl.method
    //cpg.method.fullName(file_name + ":<global>")
}

/**
  * Filter the given nodes for those that are reachable within the method.
  * 
  * (Note: this does not check for unreachable code due to always-false conditions)
  *
  * @param nodes
  *     an `Iterator[? <: AstNode]`
  * @return
  *     an `Iterator[? <: AstNode]` which contains only those nodes that are reachable
  */
def is_part_of_containing_methods_execution(nodes: Iterator[? <: AstNode]): Iterator[? <: AstNode] = {
    // assumption that we are always working with CALL nodes

    nodes.isCfgNode.filter(node => 
        // is part of method CFG
        node.method.dominates.exists(_.equals(node)) 
        // add potential other possibilities like this:
        //|| <condition>
    )
}

/**
  * Finds callback uses of the given methods.
  *
  * @param cpg
  *     the CPG instance
  * @param methods
  *     an `Iterator[Method]` with methods which may or may not be passed as callbacks
  * @return
  *     an `Iterator[Call]` which contains method calls that take callbacks for any of the given methods
  */
def get_calls_via_callbacks(cpg: Cpg, methods: Iterator[Method]): Iterator[Call] = {
    // get names to search for (ignore file methods)
    val method_names = methods.whereNot(_.name("<global>")).name.l

    // (incomplete) list of methods that take callbacks
    val callback_methods = Map(
        //apply_filters( ‘wp_privacy_personal_data_exporters’, array $args )
        //apply_filters( ‘wp_privacy_personal_data_erasers’, array $args )
        "Custom_Background::__construct" -> List(1,2),
        "Custom_Image_Header::__construct" -> List(1,2),
        //WP_Screen::add_help_tab( array $args ),
        //apply_filters( ‘site_status_tests’, array[] $tests )
        "wp_add_dashboard_widget" -> List(3,4),
        "wp_dashboard_cached_rss_widget" -> List(2),
        "add_option_update_handler" -> List(3),
        "remove_option_update_handler" -> List(3),
        "add_object_page" -> List(5),
        "add_utility_page" -> List(5),
        //_wp_handle_upload( array $file, array|false $overrides = false, string|null $time = null )
        "register_importer" -> List(4),
        "wp_iframe" -> List(1),
        //post_submit_meta_box( WP_Post $post, array $args = array() )
        //post_format_meta_box( WP_Post $post, array $box )
        //post_tags_meta_box( WP_Post $post, array $box )
        //post_categories_meta_box( WP_Post $post, array $box )
        //wp_nav_menu_item_post_type_meta_box( string $data_object, array $box )
        //wp_nav_menu_item_taxonomy_meta_box( string $data_object, array $box )
        "add_menu_page" -> List(5),
        "add_submenu_page" -> List(6),
        "add_management_page" -> List(5),
        "add_options_page" -> List(5),
        "add_theme_page" -> List(5),
        "add_plugins_page" -> List(5),
        "add_users_page" -> List(5),
        "add_dashboard_page" -> List(5),
        "add_posts_page" -> List(5),
        "add_media_page" -> List(5),
        "add_links_page" -> List(5),
        "add_pages_page" -> List(5),
        "add_comments_page" -> List(5),
        "add_meta_box" -> List(3), // !!! IS THIS WHAT ALL THE OTHER ONES TAKE THE ARRAY FOR
        //do_block_editor_incompatible_meta_box( mixed $data_object, array $box )
        "add_settings_section" -> List(3),
        "add_settings_field" -> List(3),
        //register_block_bindings_source( string $source_name, array $source_properties )
        "apply_block_hooks_to_content" -> List(3),
        "apply_block_hooks_to_content_from_post_object" -> List(3),
        "make_before_block_visitor" -> List(3),
        "make_after_block_visitor" -> List(3),
        "traverse_and_serialize_block" -> List(2,3),
        "traverse_and_serialize_blocks" -> List(2,3),
        //wp_generate_tag_cloud( WP_Term[] $tags, string|array $args = '' )
        //WP_Block_Bindings_Registry::register( string $source_name, array $source_properties )
        //WP_Block_Type::__construct( string $block_type, array|string $args = array() )
        //WP_Customize_Control::__construct( WP_Customize_Manager $manager, string $id, array $args = array() )
        //WP_Customize_Panel::__construct( WP_Customize_Manager $manager, string $id, array $args = array() )
        //WP_Customize_Section::__construct( WP_Customize_Manager $manager, string $id, array $args = array() )
        //WP_Customize_Setting::__construct( WP_Customize_Manager $manager, string $id, array $args = array() )
        "register_handler" -> List(3), // can only be WP_Embed::register_handler
        "add_filter" -> List(2), // same argument index for both wp-includes/plugin.php and WP_Hook::add_filter
        "remove_filter" -> List(2), // same argument index for both wp-includes/plugin.php and WP_Hook::remove_filter
        "has_filter" -> List(2), // same argument index for both wp-includes/plugin.php and WP_Hook::has_filter
        "make_image" -> List(2), // same argument index for both WP_Image_Editor_GD and WP_Image_Editor
        //wp_list_comments( string|array $args = array(), WP_Comment[] $comments = null )
        "register_sidebar_widget" -> List(2),
        "register_widget_control" -> List(2),
        "add_custom_image_header" -> List(1,2,3),
        "add_custom_background" -> List(1,2,3),
        "wp_embed_register_handler" -> List(3),
        "map_deep" -> List(2),
        "wp_unique_filename" -> List(3),
        //apply_filters( ‘wp_unique_filename’, string $filename, string $ext, string $dir, callable|null $unique_filename_callback, string[] $alt_filenames, int|string $number )
        "wp_find_hierarchy_loop" -> List(1),
        "wp_find_hierarchy_loop_tortoise_hare" -> List(1),
        //register_meta( string $object_type, string $meta_key, array $args, string|array $deprecated = null )
        //wp_nav_menu( array $args = array() )
        //register_setting( string $option_group, string $option_name, array $args = array() )
        "unregister_setting" -> List(3),
        "add_action" -> List(2),
        "has_action" -> List(2),
        "remove_action" -> List(2),
        "register_activation_hook" -> List(2),
        "register_deactivation_hook" -> List(2),
        "register_uninstall_hook" -> List(2),
        "_wp_filter_build_unique_id" -> List(2),
        //register_post_type( string $post_type, array|string $args = array() )
        //register_rest_field( string|array $object_type, string $attribute, array $args = array() )
        "add_feed" -> List(2),
        "add_shortcode" -> List(2),
        //register_taxonomy( string $taxonomy, array|string $object_type, array|string $args = array() )
        //register_theme_feature( string $feature, array $args = array() )
        "wp_register_sidebar_widget" -> List(3),
        "wp_register_widget_control" -> List(3),
        "_register_widget_update_callback" -> List(2),
        "_register_widget_form_callback" -> List(3),
        //do_action( ‘dynamic_sidebar’, array $widget )
        "is_active_widget" -> List(1),
        //WP_Customize_Partial::__construct( WP_Customize_Selective_Refresh $component, string $id, array $args = array() )
        //WP_Font_Utils::sanitize_from_schema( array $tree, array $schema )
        "WP_Font_Utils::apply_sanitizer" -> List(2),
        "WP_HTML_Token::__construct" -> List(4),
        "FilteredIterator::__construct" -> List(2),
        //apply_filters( ‘widget_nav_menu_args’, array $nav_menu_args, WP_Term $nav_menu, array $args, array $instance )
    )

    val callback_calls_wip = cpg.call.filter(node => callback_methods.keys.exists(_.contains(node.name))).l
    
    val callback_calls = cpg.call
        // get relevant methods
        .filter(node => callback_methods.keys.exists(_.equals(node.name)))
        // filter relevant CALLs by identifying relevant METHOD-names in corresponding callbacks
        .filter(node => callback_methods(node.name).forall(callback_index => {
            if node.argument.size < callback_index then 
                false // parameter may be optional, e.g., `has_action`
            else 
                node.argument(callback_index) // get callback
            match { // get method name according to callback type
    
                // "classname::methodname" as string (LITERAL) -- only possible for static methods
                case x if x.isLiteral && x.code.contains("::")
                    =>  method_names.exists(
                            name => x.code.endsWith("::" + name)
                            // TODO: also check if classname matches method's containing/inheriting TYPE_DECL
                        )
                
                // "methodname" as string (LITERAL)
                case x if x.isLiteral
                    =>  method_names.exists(name => x.code.contains(name))
                
                // callback using array()
                // `x.astChildren.order(3).cast[Call].argument(2)` reveals either object (IDENTIFIER) or (only if method is static) classname (LITERAL) // TODO
                // `x.astChildren.order(2).cast[Call].argument(2)` reveals method name (LITERAL)
                case x if x.isBlock
                    =>  method_names.exists(
                                name => x.astChildren.order(3).cast[Call].argument(2).exists(node => node.code.contains("\"" + name + "\"") || node.code.contains("'" + name + "'"))
                            // TODO: handle other argument
                                // TODO: check if IDENTIFIER's object is instance of method's containing/inheriting TYPE_DECL
                                // TODO: check if LITERAL matches method's containing inheriting TYPE_DECL
                        )
                
                // object with `__invoke` method (IDENTIFIER)
                case x if x.isIdentifier
                    =>  false 
                        // TODO: check if IDENTIFIER's object is instance of a class with defined `__invoke` method
                
                // unknown type of callback ?
                case _
                    =>  false

                }
        }))
    callback_calls
}

/**
  * Finds calls of methods which were defined via anonymous function variable assignment.
  *
  * @param cpg
  *     the CPG instance
  * @param methods
  *     an `Iterator[Method]` of anonymous functions
  * @return
  *     an `Iterator[Call]` which contains calls to relevant anonymous functions
  */
def get_calls_via_variable_assignment(cpg: Cpg, methods: Iterator[Method]): Iterator[Call | Nothing] = {
    // TODO
    return Iterator()
}

/**
  * Filter the given nodes for those that are part of a condition (for either a control structure or an inline if-statement).
  *
  * @param cpg
  *     the CPG instance
  * @param relevant_nodes
  *     an `Iterator[? <: AstNode]`
  * @return
  *     an `Iterator[? <: AstNode]` which contains only those that are part of a condition
  */
def is_part_of_condition(cpg: Cpg, relevant_nodes: Iterator[? <: AstNode]): Iterator[? <: AstNode] = {
    val compareSet = relevant_nodes.toSet

    cpg.controlStructure.condition.ast.filter(node => compareSet contains node)
    ++ cpg.call.name("<operator>.conditional").argument(1).filter(node => !node.cfgNext.equals(node._argumentIn)).ast.filter(node => compareSet contains node)
    // if there are other possibilities
    //++ <query>
}

/**
  * Finds paths by which execution of any source node may lead to execution of any sink node (after execution of any intermediaries).
  * 
  * (Note: this does not evaluate expressions in conditions of control structures)
  *
  * @param cpg
  *     the CPG instance
  * @param sink_nodes
  *     an `Iterator[? <: AstNode]` of sink nodes
  * @param source_nodes
  *     an `Iterator[? <: AstNode]` of source nodes
  * @param print (optional - default: false)
  * @param callResolver (implicit)
  *     required for successful compilation
  * @return
  *     to be decided ...
  */
def due_to(cpg: Cpg, sink_nodes: Iterator[? <: AstNode], source_nodes: Iterator[? <: AstNode], print: Boolean = false)(implicit callResolver: ICallResolver): Iterator[List[? <: AstNode]] = {
    // save as sets for reusability
    val sink_set = sink_nodes.toSet
    val source_set = source_nodes.toSet

    if print then
        println("STARTING SEARCH AT SINKS:")
        sink_set.foreach(node => println(s"    ${Show.default.apply(node)}"))
        println("SEARCHING FOR:")
        source_set.foreach(node => println(s"    ${Show.default.apply(node)}"))
        println("")

    // save followed paths (List) in Set
    val result: scala.collection.mutable.Set[List[? <: AstNode]] = scala.collection.mutable.Set()
    // search backwards from sink to find a source (bc less potential branching)
    var search_set: Set[(? <: AstNode, List[? <: AstNode])] = sink_set.zip(sink_set.map(node => List(node))) // Set of (node, path) tuples
    // keep track of already visited search nodes to prevent potential loops
    var visited_set: Set[? <: AstNode] = Set()
    // keep track of "discarded" paths in order to later check if their last node is contained in any successfully followed paths
    var discarded_paths: Set[List[? <: AstNode]] = Set()

    breakable {
        while search_set.size > 0 do

            if print && !search_set.map(entry => entry(0)).equals(sink_set) then
                println("------------------------------ LOOP")
                println("NEW SEARCH FOR:")
                search_set.foreach(entry => println(s"    ${Show.default.apply(entry(0))}"))
                println("")
            
            // add to visited nodes
            visited_set = visited_set ++ search_set.map(entry => entry(0))
            // nodes to search in next loop
            var new_search_set: scala.collection.mutable.Set[(? <: AstNode, List[? <: AstNode])] = scala.collection.mutable.Set()

            // SEARCH for direct CFG connection: execution of source node can lead to execution of target node within the same method
            var found_source_set: Set[? <: AstNode] = Set()
            search_set.foreach((x,prevPath) => {
                if !source_set.contains(x) then 
                x.isCfgNode.dominatedBy.foreach(y => { 
                    if source_set.contains(y) then {
                        // add last "due to" connection to result
                        result.add(prevPath :+ y)
                        // remove node `x` from further searches in current loop
                        found_source_set = found_source_set ++ Set(x) // for some reason, using `+` or `incl` for single element does not compile
                        if print then println("FOUND - SEE RESULT")
                    }
                })
                else 
                    // technically possible if the given sources are calls of an internal method
                    result.add(prevPath)
                    found_source_set = found_source_set ++ Set(x)
                    if print then println("FOUND - SEE RESULT")
            })
            
            search_set = search_set.filter(entry =>
                // no direct CFG connection -> remaining consequence nodes may be part of *methods* that are called "due to" a source node
                !found_source_set.contains(entry(0)) &&
                // check: nodes must be reachable within method in order for method call to be relevant
                is_part_of_containing_methods_execution(Iterator.single(entry(0))).toSet.contains(entry(0))
            )
            if search_set.size <= 0 then break

            //println("\n------------------\nNEW SET OF METHODS\n------------------")

            // SEARCH for call nodes
            search_set.foreach((x,prevPath) => {
                if print then
                    println("SEARCHING FOR CALLS OF METHOD:")
                    x.isCfgNode.method.foreach( method_node =>
                        println(s"    ${Show.default.apply(method_node)}")
                    )

                x.isCfgNode.method.foreach(method_node => 
                    if !visited_set.contains(method_node) then 
                    // add to visited nodes
                    visited_set = visited_set ++ Set(method_node)
                    
                    // collect calls for next loop
                    var found: Boolean = false // for printing
                    get_calls_for_methods(cpg, method_node).foreach(call =>
                        val new_path = prevPath :+ method_node :+ call
                        if !visited_set.contains(call) then 
                            new_search_set.add(call, new_path)
                        else 
                            discarded_paths = discarded_paths ++ Set(new_path)
                        if print then found = true
                    )
                    if print then
                        if found then 
                            println("FOUND CALLS")
                            get_calls_for_methods(cpg, method_node).foreach(call => println(s"    ${Show.default.apply(call)}"))
                        else
                            println("FOUND NO CALLS")
                        println("")
                    else
                        discarded_paths = discarded_paths ++ Set(prevPath :+ method_node)
                )
            })

            //println(s"search_set.size: ${new_search_set.size}" + "\n" + s"new_search_set.size: ${new_search_set.size}" + "\n" + s"diff size: ${new_search_set.map(entry => entry(0)).diff(search_set.map(entry => entry(0))).size}" + "\n" + s"discarded_paths.size: ${discarded_paths.size}")

            // prepare next loop
            search_set = new_search_set.toSet
    }

    // add to result any "discarded" paths that would have found a source
    result.toSet.foreach(found_path => {
        discarded_paths.foreach(path => {
            if found_path.contains(path.last) then
                var temp = found_path.slice(found_path.indexOf(path.last)+1,found_path.length)
                result.add(path.concat(temp))
        })
    })    

    // print result
    var counter: Int = 1
    if result.toSet.size > 0 then
        result.toSet.foreach(list => {
            println(s"Path $counter")
            list.map(node => node match 
                case x: io.shiftleft.codepropertygraph.generated.nodes.Call if sink_set.contains(x) => 
                    s"        start: ${x.code}" + "\n" +
                    //s"               |" + "\n" +
                    s"             ├── call name: ${x.name}" + "\n" +
                    s"             └── in line: ${x.lineNumber}"
                case x if sink_set.contains(x) => 
                    s"        start: ${x.code}" + "\n" +
                    //s"               |" + "\n" +
                    s"             └── in line: ${x.lineNumber}"
                case x: io.shiftleft.codepropertygraph.generated.nodes.Call if source_set.contains(x) =>
                    s"        found: ${x.code}" + "\n" +
                    //s"               |" + "\n" +
                    s"             ├── in line: ${x.lineNumber}" + "\n" +
                    s"             ├──── in method: ${x.method.code}" + "\n" +
                    s"             └────── in file: ${x.method.filename}"
                case x: io.shiftleft.codepropertygraph.generated.nodes.Call if x.isCall => 
                    s"    called by: ${x.code}" + "\n" +
                    s"             ├── call name: ${x.name}" + "\n" +
                    s"             └── in line: ${x.lineNumber}"
                case x: io.shiftleft.codepropertygraph.generated.nodes.Method if x.isMethod => 
                    s"    in method: ${x.code}" + "\n" +
                    //s"               |" + "\n" +
                    s"             └── in file: ${x.filename}"
                case _ => 
                    s"        found: ${node.code}"
            ).foreach(content => println(content))
            println("")
            counter += 1
        })
    else
        println("No paths found!")

    result.toSet.iterator

}


def due_to_is_admin(cpg: Cpg, sink_nodes: Iterator[? <: AstNode], print: Boolean = false)(implicit callResolver: ICallResolver): Iterator[List[? <: AstNode]] = {
    due_to(cpg, sink_nodes, cpg.call.name("is_admin"), print)
}
