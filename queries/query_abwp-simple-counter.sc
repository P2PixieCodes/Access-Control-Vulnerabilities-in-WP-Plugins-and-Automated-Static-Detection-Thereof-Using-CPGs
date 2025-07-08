
/* COPIED FROM draft.sc */

/**
  * Given a file <global> method, get all includes/requires of the corresponding file.
  *
  * @param methods --- an `Iterator[Method]` which may or may not contain `filename.php:<global>` methods
  * @return an `Iterator[Call]` of `include`/`require` statements corresponding to any `<global>` methods; 
  *         if there are no `<global>` methods, the iterator is empty
  */
def get_include_require_calls(methods: Iterator[Method]): Iterator[Call] = {

    // we assume that files' global methods are named `filename.php:<global>`
    def file_methods = methods.filter(_.name.contains("<global>"))
    
    val all_file_calls = cpg.call.or(_.name("include"),_.name("include_once"),_.name("require"),_.name("require_once")).l

    // we assume that the entire filename is passed as a string literal to the statement
    def relevant_file_calls = all_file_calls.iterator.filter(node => global_methods.exists(method_node => node.code.contains(method_nodenode.filename)))

    // TODO: case where the filename/path is dynamically constructed
    
    relevant_file_calls
}

/**
  * Given a filename, get the file's `<global>` method.
  *
  * @param main_file_name --- the given filename as a `String`
  * @return an `Iterator[Method]` with the corresponding `<global>` file method;
  *         if there is no file with that name, the iterator is empty
  */
def get_entry_method(file_name: String): Iterator[Method] = {
    cpg.file(main_file_name).namespaceBlock.typeDecl.method
    //cpg.method.fullName(file_name + ":<global>")
}

/**
  * Filter the given CALLs for those that are reachable within the method.
  * 
  * (Note: this does not check for unreachable code due to always-false conditions)
  *
  * @param calls --- an `Iterator[Call]`
  * @return an `Iterator[Call]` which contains only those nodes that are reachable
  */
def is_part_of_containing_methods_execution(calls: Iterator[Call]): Iterator[Call] = {
    // assumption that we are always working with CALL nodes

    calls.filter(node => 
        // is part of method CFG
        node.method.dominates.exists(_.equals(node)) 
        // add potential other possibilities like this:
        //|| <condition>
    )
}

/**
  * Finds callback uses of the given methods.
  *
  * @param methods --- an `Iterator[Method]` with methods which may or may not be passed as callbacks
  * @return an `Iterator[Call]` which contains method calls that take callbacks for any of the given methods
  */
def get_calls_via_callbacks(methods: Iterator[Method]): Iterator[Call] = {
    // get names to search for (ignore file methods)
    val method_names = methods.whereNot(_.name("<global>")).name.l

    // (incomplete) list of methods that take callbacks
    val callback_methods = Map(
        "add_action" -> 2, 
        "add_options_page" -> 5
    )

    val callback_calls_wip = cpg.call.filter(node => callback_methods.keys.exists(_.contains(node.name))).l
    
    val callback_calls = cpg.call
        // get relevant methods
        .filter(node => callback_methods.keys.exists(_.contains(node.name)))
        // filter relevant CALLs by identifying relevant METHOD-names in corresponding callbacks
        .filter(node => node.argument(callback_methods(node.name)) // get callback
            match { // get method name according to callback type
    
                // "classname::methodname" as string (LITERAL) -- only possible for static methods
                case x if x.isLiteral && x.code.contains("::")
                    => false // TODO
                // "methodname" as string (LITERAL)
                case x if x.isLiteral
                    => method_names.exists(name => x.code.contains(name))
                // callback using array()
                // `x.astChildren.order(3).cast[Call].argument(2)` reveals either object (IDENTIFIER) or (only if method is static) classname (LITERAL) // TODO
                // `x.astChildren.order(2).cast[Call].argument(2)` reveals method name (LITERAL)
                case x if x.isBlock
                    => method_names.exists(name => x.astChildren.order(3).cast[Call].argument(2).next.code.contains(name))
                // object with `__invoke` method (IDENTIFIER)
                case x if x.isIdentifier
                    => false // TODO
                // unknown type of callback ?
                case _
                    => false
            }
        )
}