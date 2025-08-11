import scala.collection.mutable.ListBuffer
import io.shiftleft.codepropertygraph.generated.*
import io.shiftleft.codepropertygraph.generated.nodes.*
import io.shiftleft.codepropertygraph.generated.help.*
import io.shiftleft.codepropertygraph.generated.language.*
import io.shiftleft.codepropertygraph.generated.accessors.*
import io.shiftleft.semanticcpg.*
import io.shiftleft.semanticcpg.language.*
import flatgraph.traversal.*

/* abwp-simple-counter.1.0.2

SINKS
includes/page-admin-counters.php:111:15
└── `submit_button()` submits form with unsanitised input
*/
def plugin_00 = List(
    due_to_is_admin(cpg, cpg.call.name("submit_button")),
    check_paths_for_capability_checks(cpg, cpg.call.name("submit_button"), cpg.call.name("is_admin"))
)

/* 3com-asesor-de-cookies
SINKS
html/admin/principal.html:386:29
└── combined with html/admin/principal.js (lines 22-68 and 129-131)
    └── clicking element with selector "a.cdp-cookies-guardar" sends POST with unsanitised input

ADJUSTMENTS
Joern's php2cpg does not parse `*.html` files.
    Thus, the actual sink cannot be queried.
    Instead, attempt to find any calls referencing `principal.html` and use those as sinks.
*/
def plugin_01 = List(
    due_to_is_admin(cpg, cpg.call.filter(_.code.contains("principal.html"))),
    check_paths_for_capability_checks(cpg, cpg.call.filter(_.code.contains("principal.html")), cpg.call.name("is_admin"))
)



/* 404-solution.2.34.0

SINKS
includes/DataAccess.php
└── any call of `$wpdb->get_results`
    └── using results of `ABJ_404_Solution_DataAccess::getRedirectsForViewQuery`
        └── SQL request with unsanitised data

ADJUSTMENTS
Didn't bother to follow data-flow to ensure that the calls are related.
*/
def plugin_02 = List(
    //due_to(cpg, cpg.call.name("get_results"), cpg.call.name("is_admin")),
    due_to(cpg, cpg.call.name("get_results"), get_calls_for_methods(cpg, cpg.method.name("getRedirectsForViewQuery"))),
    due_to_is_admin(cpg, get_calls_for_methods(cpg, cpg.method.name("getRedirectsForViewQuery"))),
    check_paths_for_capability_checks(cpg, cpg.call.name("get_results"), get_calls_for_methods(cpg, cpg.method.name("getRedirectsForViewQuery"))),
    check_paths_for_capability_checks(cpg, get_calls_for_methods(cpg, cpg.method.name("getRedirectsForViewQuery")), cpg.call.name("is_admin"))
)

/* 404-solution.2.35.7

SINKS
includes/DataAccess.php
└── any call of `$wpdb->get_results`
    └── due to call of `ABJ_404_Solution_DataAccess::getLogRecords`
        └── SQL request with unsanitised data
*/
def plugin_03 = List(
    //due_to(cpg, cpg.call.name("get_results"), cpg.call.name("is_admin")),
    due_to(cpg, cpg.call.name("get_results"), get_calls_for_methods(cpg, cpg.method.name("getLogRecords"))),
    due_to_is_admin(cpg, get_calls_for_methods(cpg, cpg.method.name("getLogRecords")))
    check_paths_for_capability_checks(cpg, cpg.call.name("get_results"), get_calls_for_methods(cpg, cpg.method.name("getLogRecords"))),
    check_paths_for_capability_checks(cpg, get_calls_for_methods(cpg, cpg.method.name("getLogRecords")), cpg.call.name("is_admin"))
)


/* accelerated-mobile-pages.1.0.77.31

SINKS
pagebuilder/components/pbSettingTemplates.php:198
└── pagebuilder/inc/admin-amp-page-builder.js
    └── `savePagebuilderSettings` sets `script_data` field with unsanitised input
includes/options/admin-config.php:3387
└── the textarea input field

SOURCES
accelerated-moblie-pages.php (lines 532-543)
└── `ampforwp_include_options_file` checks `is_admin` before including various files
        (This was manually verified while investigating the vulnerability cause: replacing the check 
         with a stricter capability prevents the options page from being accessible to site admins)

ADJUSTMENTS
Connection between input field and save functionality is very opaque and seems to involve different frameworks and languages.
    Thus, the CPG generated from the PHP files does not contain all plugin features
    Instead, we manually determine 
        (a) where the input data is saved to the plugin, and
        (b) where the input field is made available to the user
Relevant `is_admin`-check is added to action hook.
    Thus, we cannot use the queries (as they are now available) to say whether the call is connected
    Instead, we rely on the fact that manual investigation of the vulnerability identified it as a key element
*/
def plugin_04 = List(
    //due_to_is_admin(cpg, cpg.call.filter(_.code.contains("savePagebuilderSettings"))),
    //due_to_is_admin(cpg, cpg.call.filter(_.code.contains("Enter HTML in Body (beginning of body tag)"))),
    due_to(cpg, cpg.call.filter(_.code.contains("savePagebuilderSettings")), get_calls_for_methods(cpg, cpg.method.name("ampforwp_include_options_file"))),
    check_paths_for_capability_checks(cpg, cpg.call.filter(_.code.contains("savePagebuilderSettings")), get_calls_for_methods(cpg, cpg.method.name("ampforwp_include_options_file")))
)

/* accelerated-mobile-pages.1.0.77.32

SINKS - same as above
SOURCES - same as above
ADJUSTMENTS - same as above

NOTES
They tried to fix the vulnerability in the previous version by simply deleting `<script>` tags, which was quite easy to circumvent.
*/
def plugin_05 = List(
    //due_to_is_admin(cpg, cpg.call.filter(_.code.contains("savePagebuilderSettings"))),
    //due_to_is_admin(cpg, cpg.call.filter(_.code.contains("Enter HTML in Body (beginning of body tag)"))),
    due_to(cpg, cpg.call.filter(_.code.contains("savePagebuilderSettings")), get_calls_for_methods(cpg, cpg.method.name("ampforwp_include_options_file"))),
    check_paths_for_capability_checks(cpg, cpg.call.filter(_.code.contains("savePagebuilderSettings")), get_calls_for_methods(cpg, cpg.method.name("ampforwp_include_options_file")))
)


/* accordions-or-faqs.2.0.3

SINKS
Includes/Settings.php:100
└── input element with `id="accordions_or_faqs_license_key"`
*/
def plugin_06 = List(
    due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("accordions_or_faqs_license_key"))),
    check_paths_for_capability_checks(cpg, cpg.call.name("echo").filter(_.code.contains("accordions_or_faqs_license_key")), cpg.call.name("is_admin"))
)


/* aco-product-labels-for-woocommerce.1.5.8

SINKS
aco-product-labels-for-woocommerce.1.5.8/aco-product-labels-for-woocommerce/includes/class-acoplw-backend.php
└── ACOPLW_Backend::view
    ├── combined with /assets/js/backend.js
    └── includes a '/includes/views/admin-*.php' file, the contents of which are set by the javascript file
        └── one of these allows access to badge settings pages through which unsanitised input can be saved

ADJUSTMENTS
The contents of the settings pages are dynamically generated with javascript.
    Thus, their content is not available in the CPG.
    Instead, we rely on the fact that manual investigation of the vulnerability identified these files as relevant
The file inclusions are dynamically generated from arguments.
    Thus, we cannot use the queries (as they are now available) to determine where the files are included.
    Instead, we rely on the fact that manual investigation of the vulnerability identified the inclusions via `ACOPLW_Backend::view` as relevant.
*/
def plugin_07 = List(
    due_to_is_admin(cpg, get_calls_for_methods(cpg, cpg.method.fullName("ACOPLW_Backend<metaclass>.view"))),
    check_paths_for_capability_checks(cpg, get_calls_for_methods(cpg, cpg.method.fullName("ACOPLW_Backend<metaclass>.view")), cpg.call.name("is_admin"))
)


/* add-to-any.1.7.45

SINKS
addtoany.admin.php
└── submit button (line 874) submits form with unsanitised input (line 569)
*/
def plugin_08 = List(
    due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("A2A_SHARE_SAVE_header"))),
    due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("""<input class=\"button-primary\" type=\"submit\" name=\"Submit\" value=\""""))),
    check_paths_for_capability_checks(cpg, cpg.call.name("echo").filter(_.code.contains("A2A_SHARE_SAVE_header")), cpg.call.name("is_admin")),
    check_paths_for_capability_checks(cpg, cpg.call.name("echo").filter(_.code.contains("""<input class=\"button-primary\" type=\"submit\" name=\"Submit\" value=\"""")), cpg.call.name("is_admin"))
)

/* add-to-any.1.7.47

SINKS
addtoany.admin.php
└── submit button (line 874) submits form with unsanitised input (line 563)
*/
def plugin_09 = List(
    due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("A2A_SHARE_SAVE_button_custom"))),
    due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("""<input class=\"button-primary\" type=\"submit\" name=\"Submit\" value=\""""))),
    check_paths_for_capability_checks(cpg, cpg.call.name("echo").filter(_.code.contains("A2A_SHARE_SAVE_button_custom")), cpg.call.name("is_admin")),
    check_paths_for_capability_checks(cpg, cpg.call.name("echo").filter(_.code.contains("""<input class=\"button-primary\" type=\"submit\" name=\"Submit\" value=\"""")), cpg.call.name("is_admin"))
)

/* add-to-feedly.1.2.11

SINKS
addtofeedly.php
└── `submit_button()` (line 80) submits form with unsanitised input (line 55)
*/
def plugin_10 = List(
    due_to_is_admin(cpg, cpg.call.name("submit_button")),
    check_paths_for_capability_checks(cpg, cpg.call.name("submit_button"), cpg.call.name("is_admin"))
)

/* add-whatsapp-button.2.1.7

SINKS
admin/settings.php:335
└── uses `sanitize_text_field()` to set content of `value` attribute
*/
def plugin_11 = List(
    due_to_is_admin(cpg, cpg.call.name("sanitize_text_field")),
    check_paths_for_capability_checks(cpg, cpg.call.name("sanitize_text_field"), cpg.call.name("is_admin"))
)


/* ad-injection.1.2.0.19

SINKS
ui-tab-main.php
└── combined with ad-injection-admin.php
    └── `_e`-calls with `Save all settings` submit forms with unsanitised input (ui-tab-main.php:430)
*/
def plugin_12 = List(
    due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("""<textarea name=\"ad_code_top_1\""""))),
    due_to_is_admin(cpg, cpg.call.name("_e").filter(_.code.contains("Save all settings"))),
    check_paths_for_capability_checks(cpg, cpg.call.name("echo").filter(_.code.contains("""<textarea name=\"ad_code_top_1\"""")), cpg.call.name("is_admin")),
    check_paths_for_capability_checks(cpg, cpg.call.name("_e").filter(_.code.contains("Save all settings")), cpg.call.name("is_admin"))
)