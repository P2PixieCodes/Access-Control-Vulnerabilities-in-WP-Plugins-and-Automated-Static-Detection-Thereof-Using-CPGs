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
def plugin_00 = due_to(cpg, cpg.call.name("submit_button"), cpg.call.name("is_admin")).l
/* QUERY RESULT AS OF 11.07.2025 15:56

joern> due_to(cpg, cpg.call.name("submit_button"), cpg.call.name("is_admin"))
Path 1
        start: submit_button()
             ├── call name: submit_button
             └── in line: Some(111)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: includes/page-admin-counters.php
    called by: include plugin_dir_path(__FILE__) . "page-admin-counters.php"
             ├── call name: include
             └── in line: Some(12)
    in method: PUBLIC function view(this)
             └── in file: includes/admin-counters.php
    called by: add_options_page(__("Simple Counter","abwp-simple-counter"),__("Simple Counter","abwp-simple-counter"),"manage_options","simple-counter",)
             ├── call name: add_options_page
             └── in line: Some(49)
    in method: PUBLIC function admin_menu(this)
             └── in file: simple-counter.php
    called by: add_action("admin_menu",)
             ├── call name: add_action
             └── in line: Some(37)
    in method: PRIVATE function define_admin_hooks(this)
             └── in file: simple-counter.php
    called by: $this->define_admin_hooks()
             ├── call name: define_admin_hooks
             └── in line: Some(23)
        found: is_admin()
             ├── in method: PUBLIC function __construct(this)
             └──── in file: simple-counter.php

val res5:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/

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
def plugin_01 = due_to(cpg, cpg.call.filter(_.code.contains("principal.html")), cpg.call.name("is_admin")).l
/* QUERY RESULT AS OF 11.07.2025 15:57

joern> due_to(cpg, cpg.call.filter(_.code.contains("principal.html")), cpg.call.name("is_admin"))
Path 1
        start: require_once CDP_COOKIES_DIR_HTML . "admin/principal.html"
             ├── call name: require_once
             └── in line: Some(542)
    in method: STATIC function pag_configuracion()
             └── in file: lib/plugin.php
    called by: add_submenu_page("tools.php","Asesor de cookies","Asesor de cookies","manage_options","cdp_cookies",)
             ├── call name: add_submenu_page
             └── in line: Some(526)
    in method: STATIC function crear_menu_admin()
             └── in file: lib/plugin.php
    called by: add_action("admin_menu",)
             ├── call name: add_action
             └── in line: Some(37)
        found: is_admin()
             ├── in method: STATIC function ejecutar()
             └──── in file: 3com_cookies/lib/plugin.php

Path 2
        start: require_once CDP_COOKIES_DIR_HTML . "admin/principal.html"
             ├── call name: require_once
             └── in line: Some(542)
    in method: STATIC function pag_configuracion()
             └── in file: lib/plugin.php
    called by: add_submenu_page("tools.php","Asesor de cookies","Asesor de cookies","manage_options","cdp_cookies",)
             ├── call name: add_submenu_page
             └── in line: Some(526)
    in method: STATIC function crear_menu_admin()
             └── in file: lib/plugin.php
    called by: add_action("admin_menu",)
             ├── call name: add_action
             └── in line: Some(37)
        found: is_admin()
             ├── in method: STATIC function ejecutar()
             └──── in file: lib/plugin.php

Path 3
        start: require_once CDP_COOKIES_DIR_HTML . "admin/principal.html"
             ├── call name: require_once
             └── in line: Some(523)
    in method: STATIC function pag_configuracion()
             └── in file: 3com_cookies/lib/plugin.php
    called by: add_submenu_page("tools.php","Asesor de cookies","Asesor de cookies","manage_options","cdp_cookies",)
             ├── call name: add_submenu_page
             └── in line: Some(507)
    in method: STATIC function crear_menu_admin()
             └── in file: 3com_cookies/lib/plugin.php
    called by: add_action("admin_menu",)
             ├── call name: add_action
             └── in line: Some(37)
        found: is_admin()
             ├── in method: STATIC function ejecutar()
             └──── in file: 3com_cookies/lib/plugin.php

Path 4
        start: require_once CDP_COOKIES_DIR_HTML . "admin/principal.html"
             ├── call name: require_once
             └── in line: Some(523)
    in method: STATIC function pag_configuracion()
             └── in file: 3com_cookies/lib/plugin.php
    called by: add_submenu_page("tools.php","Asesor de cookies","Asesor de cookies","manage_options","cdp_cookies",)
             ├── call name: add_submenu_page
             └── in line: Some(507)
    in method: STATIC function crear_menu_admin()
             └── in file: 3com_cookies/lib/plugin.php
    called by: add_action("admin_menu",)
             ├── call name: add_action
             └── in line: Some(37)
        found: is_admin()
             ├── in method: STATIC function ejecutar()
             └──── in file: lib/plugin.php

val res3:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/

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
    due_to(cpg, get_calls_for_methods(cpg, cpg.method.name("getRedirectsForViewQuery")), cpg.call.name("is_admin"))
)
/* RESULT AS OF 11.07.2025 16:30

joern> due_to(cpg, cpg.call.name("get_results"), get_calls_for_methods(cpg, cpg.method.name("getRedirectsForViewQuery")))
Path 1
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(1980)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $this->queryAndGetResults("set session sql_big_selects = 1",$ignoreErrorsOoptions)
             ├── call name: queryAndGetResults
             └── in line: Some(782)
        found: $this->getRedirectsForViewQuery($sub,$tableOptions,$queryAllRowsAtOnce,$limitStart,$limitEnd,false)
             ├── in method: function getRedirectsForView(this,$sub,$tableOptions)
             └──── in file: includes/DataAccess.php

Path 2
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(1980)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $this->queryAndGetResults($query)
             ├── call name: queryAndGetResults
             └── in line: Some(812)
        found: $this->getRedirectsForViewQuery($sub,$tableOptions,false,0,PHP_INT_MAX,true)
             ├── in method: function getRedirectsForViewCount(this,$sub,$tableOptions)
             └──── in file: includes/DataAccess.php

Path 3
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(1980)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $this->queryAndGetResults($query)
             ├── call name: queryAndGetResults
             └── in line: Some(949)
    in method: function maybeUpdateRedirectsForViewHitsTable(this)
             └── in file: includes/DataAccess.php
    called by: $this->maybeUpdateRedirectsForViewHitsTable()
             ├── call name: maybeUpdateRedirectsForViewHitsTable
             └── in line: Some(859)
    in method: function getRedirectsForViewQuery(this,$sub,$tableOptions,$queryAllRowsAtOnce,$limitStart,$limitEnd,$selectCountOnly)
             └── in file: includes/DataAccess.php
        found: $this->getRedirectsForViewQuery($sub,$tableOptions,$queryAllRowsAtOnce,$limitStart,$limitEnd,false)
             ├── in method: function getRedirectsForView(this,$sub,$tableOptions)
             └──── in file: includes/DataAccess.php

Path 4
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(1980)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $this->queryAndGetResults($query)
             ├── call name: queryAndGetResults
             └── in line: Some(2027)
    in method: function updatePermalinkCache(this)
             └── in file: includes/DataAccess.php
    called by: $permalinkCache->updatePermalinkCache(1)
             ├── call name: updatePermalinkCache
             └── in line: Some(468)
    in method: function updateToNewVersion(this,$options)
             └── in file: includes/PluginLogic.php
    called by: $this->updateToNewVersion($options)
             ├── call name: updateToNewVersion
             └── in line: Some(429)
    in method: function getOptions(this,$skip_db_check)
             └── in file: includes/PluginLogic.php
    called by: $abj404logic->getOptions(true)
             ├── call name: getOptions
             └── in line: Some(32)
    in method: function isDebug(this)
             └── in file: includes/Logging.php
    called by: $this->isDebug()
             ├── call name: isDebug
             └── in line: Some(79)
    in method: function debugMessage(this,$message,$e)
             └── in file: includes/Logging.php
    called by: $logger->debugMessage("Found " . $foundRowsBeforeLogsData . " rows to display before log data and " . count($rows) . " rows to display after log data for page: " . $sub)
             ├── call name: debugMessage
             └── in line: Some(791)
        found: $this->getRedirectsForViewQuery($sub,$tableOptions,$queryAllRowsAtOnce,$limitStart,$limitEnd,false)
             ├── in method: function getRedirectsForView(this,$sub,$tableOptions)
             └──── in file: includes/DataAccess.php

Path 5
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(1980)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $this->queryAndGetResults($query)
             ├── call name: queryAndGetResults
             └── in line: Some(1056)
    in method: function getLogsIDandURL(this,$specificURL)
             └── in file: includes/DataAccess.php
    called by: $this->getLogsIDandURL($row["url"])
             ├── call name: getLogsIDandURL
             └── in line: Some(1029)
    in method: function populateLogsData(this,$rows)
             └── in file: includes/DataAccess.php
    called by: $this->populateLogsData($rows)
             ├── call name: populateLogsData
             └── in line: Some(789)
        found: $this->getRedirectsForViewQuery($sub,$tableOptions,$queryAllRowsAtOnce,$limitStart,$limitEnd,false)
             ├── in method: function getRedirectsForView(this,$sub,$tableOptions)
             └──── in file: includes/DataAccess.php

Path 6
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(1980)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $this->queryAndGetResults("set session max_join_size = 18446744073709551615",$ignoreErrorsOoptions)
             ├── call name: queryAndGetResults
             └── in line: Some(780)
        found: $this->getRedirectsForViewQuery($sub,$tableOptions,$queryAllRowsAtOnce,$limitStart,$limitEnd,false)
             ├── in method: function getRedirectsForView(this,$sub,$tableOptions)
             └──── in file: includes/DataAccess.php

Path 7
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(1980)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $this->queryAndGetResults($query)
             ├── call name: queryAndGetResults
             └── in line: Some(2040)
    in method: function updatePermalinkCacheParentPages(this)
             └── in file: includes/DataAccess.php
    called by: $abj404dao->updatePermalinkCacheParentPages()
             ├── call name: updatePermalinkCacheParentPages
             └── in line: Some(74)
    in method: function updatePermalinkCache(this,$maxExecutionTime,$executionCount)
             └── in file: includes/PermalinkCache.php
    called by: $permalinkCache->updatePermalinkCache(1)
             ├── call name: updatePermalinkCache
             └── in line: Some(468)
    in method: function updateToNewVersion(this,$options)
             └── in file: includes/PluginLogic.php
    called by: $this->updateToNewVersion($options)
             ├── call name: updateToNewVersion
             └── in line: Some(429)
    in method: function getOptions(this,$skip_db_check)
             └── in file: includes/PluginLogic.php
    called by: $abj404logic->getOptions(true)
             ├── call name: getOptions
             └── in line: Some(32)
    in method: function isDebug(this)
             └── in file: includes/Logging.php
    called by: $this->isDebug()
             ├── call name: isDebug
             └── in line: Some(79)
    in method: function debugMessage(this,$message,$e)
             └── in file: includes/Logging.php
    called by: $logger->debugMessage("Found " . $foundRowsBeforeLogsData . " rows to display before log data and " . count($rows) . " rows to display after log data for page: " . $sub)
             ├── call name: debugMessage
             └── in line: Some(791)
        found: $this->getRedirectsForViewQuery($sub,$tableOptions,$queryAllRowsAtOnce,$limitStart,$limitEnd,false)
             ├── in method: function getRedirectsForView(this,$sub,$tableOptions)
             └──── in file: includes/DataAccess.php

Path 8
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(1980)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $this->queryAndGetResults("set session max_join_size = 18446744073709551615",$ignoreErrorsOoptions)
             ├── call name: queryAndGetResults
             └── in line: Some(809)
        found: $this->getRedirectsForViewQuery($sub,$tableOptions,false,0,PHP_INT_MAX,true)
             ├── in method: function getRedirectsForViewCount(this,$sub,$tableOptions)
             └──── in file: includes/DataAccess.php

Path 9
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(1980)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $this->queryAndGetResults($query)
             ├── call name: queryAndGetResults
             └── in line: Some(949)
    in method: function maybeUpdateRedirectsForViewHitsTable(this)
             └── in file: includes/DataAccess.php
    called by: $this->maybeUpdateRedirectsForViewHitsTable()
             ├── call name: maybeUpdateRedirectsForViewHitsTable
             └── in line: Some(859)
    in method: function getRedirectsForViewQuery(this,$sub,$tableOptions,$queryAllRowsAtOnce,$limitStart,$limitEnd,$selectCountOnly)
             └── in file: includes/DataAccess.php
        found: $this->getRedirectsForViewQuery($sub,$tableOptions,false,0,PHP_INT_MAX,true)
             ├── in method: function getRedirectsForViewCount(this,$sub,$tableOptions)
             └──── in file: includes/DataAccess.php

Path 10
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(1980)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $this->queryAndGetResults($query)
             ├── call name: queryAndGetResults
             └── in line: Some(783)
        found: $this->getRedirectsForViewQuery($sub,$tableOptions,$queryAllRowsAtOnce,$limitStart,$limitEnd,false)
             ├── in method: function getRedirectsForView(this,$sub,$tableOptions)
             └──── in file: includes/DataAccess.php

Path 11
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(1980)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $this->queryAndGetResults("set session sql_big_selects = 1",$ignoreErrorsOoptions)
             ├── call name: queryAndGetResults
             └── in line: Some(811)
        found: $this->getRedirectsForViewQuery($sub,$tableOptions,false,0,PHP_INT_MAX,true)
             ├── in method: function getRedirectsForViewCount(this,$sub,$tableOptions)
             └──── in file: includes/DataAccess.php

val res7:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator

joern> due_to(cpg, get_calls_for_methods(cpg, cpg.method.name("getRedirectsForViewQuery")), cpg.call.name("is_admin"))
Path 1
        start: $this->getRedirectsForViewQuery($sub,$tableOptions,false,0,PHP_INT_MAX,true)
             ├── call name: getRedirectsForViewQuery
             └── in line: Some(805)
    in method: function getRedirectsForViewCount(this,$sub,$tableOptions)
             └── in file: includes/DataAccess.php
    called by: $abj404dao->getRedirectsForViewCount($sub,$tableOptions)
             ├── call name: getRedirectsForViewCount
             └── in line: Some(2073)
    in method: function getPaginationLinks(this,$sub,$showSearchFilter)
             └── in file: includes/View.php
    called by: $abj404view->getPaginationLinks($subpage)
             ├── call name: getPaginationLinks
             └── in line: Some(39)
    in method: function getPaginationLinks(this)
             └── in file: includes/ajax/ViewUpdater.php
    called by: $this->getPaginationLinks($sub)
             ├── call name: getPaginationLinks
             └── in line: Some(1832)
    in method: function echoAdminLogsPage(this)
             └── in file: includes/View.php
    called by: $abj404view->echoAdminLogsPage()
             ├── call name: echoAdminLogsPage
             └── in line: Some(117)
    in method: function echoChosenAdminTab(this,$action,$sub,$message)
             └── in file: includes/View.php
    called by: $abj404view->echoChosenAdminTab($action,$sub,$message)
             ├── call name: echoChosenAdminTab
             └── in line: Some(76)
        found: is_admin()
             ├── in method: STATIC function handleMainAdminPageActionAndDisplay()
             └──── in file: includes/View.php

Path 2
        start: $this->getRedirectsForViewQuery($sub,$tableOptions,false,0,PHP_INT_MAX,true)
             ├── call name: getRedirectsForViewQuery
             └── in line: Some(805)
    in method: function getRedirectsForViewCount(this,$sub,$tableOptions)
             └── in file: includes/DataAccess.php
    called by: $abj404dao->getRedirectsForViewCount($sub,$tableOptions)
             ├── call name: getRedirectsForViewCount
             └── in line: Some(2073)
    in method: function getPaginationLinks(this,$sub,$showSearchFilter)
             └── in file: includes/View.php
    called by: $this->getPaginationLinks($sub)
             ├── call name: getPaginationLinks
             └── in line: Some(1832)
    in method: function echoAdminLogsPage(this)
             └── in file: includes/View.php
    called by: $abj404view->echoAdminLogsPage()
             ├── call name: echoAdminLogsPage
             └── in line: Some(117)
    in method: function echoChosenAdminTab(this,$action,$sub,$message)
             └── in file: includes/View.php
    called by: $abj404view->echoChosenAdminTab($action,$sub,$message)
             ├── call name: echoChosenAdminTab
             └── in line: Some(76)
        found: is_admin()
             ├── in method: STATIC function handleMainAdminPageActionAndDisplay()
             └──── in file: includes/View.php

val res8:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/

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
    due_to(cpg, get_calls_for_methods(cpg, cpg.method.name("getLogRecords")), cpg.call.name("is_admin"))
)
/* RESULT AS OF 11.07.2025 16:43

joern> due_to(cpg, cpg.call.name("get_results"), get_calls_for_methods(cpg, cpg.method.name("getLogRecords")))
Path 1
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(728)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $abj404dao->queryAndGetResults($query)
             ├── call name: queryAndGetResults
             └── in line: Some(540)
    in method: function updateToNewVersionAction(this,$options)
             └── in file: includes/PluginLogic.php
    called by: $this->updateToNewVersionAction($options)
             ├── call name: updateToNewVersionAction
             └── in line: Some(458)
    in method: function updateToNewVersion(this,$options)
             └── in file: includes/PluginLogic.php
    called by: $this->updateToNewVersion($options)
             ├── call name: updateToNewVersion
             └── in line: Some(428)
    in method: function getOptions(this,$skip_db_check)
             └── in file: includes/PluginLogic.php
    called by: $abj404logic->getOptions(true)
             ├── call name: getOptions
             └── in line: Some(32)
    in method: function isDebug(this)
             └── in file: includes/Logging.php
    called by: $this->isDebug()
             ├── call name: isDebug
             └── in line: Some(79)
    in method: function debugMessage(this,$message,$e)
             └── in file: includes/Logging.php
    called by: $abj404logging->debugMessage($logRecordsDisplayed . " log records displayed on the page.")
             ├── call name: debugMessage
             └── in line: Some(1951)
        found: $abj404dao->getLogRecords($tableOptions)
             ├── in method: function getAdminLogsPageTable(this,$sub)
             └──── in file: includes/View.php

Path 2
        start: $wpdb->get_results($query,ARRAY_A)
             ├── call name: get_results
             └── in line: Some(728)
    in method: function queryAndGetResults(this,$query,$options)
             └── in file: includes/DataAccess.php
    called by: $this->queryAndGetResults($query)
             ├── call name: queryAndGetResults
             └── in line: Some(1196)
    in method: function getLogRecords(this,$tableOptions)
             └── in file: includes/DataAccess.php
        found: $abj404dao->getLogRecords($tableOptions)
             ├── in method: function getAdminLogsPageTable(this,$sub)
             └──── in file: includes/View.php

val res11:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator

joern> due_to(cpg, get_calls_for_methods(cpg, cpg.method.name("getLogRecords")), cpg.call.name("is_admin"))
Path 1
        start: $abj404dao->getLogRecords($tableOptions)
             ├── call name: getLogRecords
             └── in line: Some(1903)
    in method: function getAdminLogsPageTable(this,$sub)
             └── in file: includes/View.php
    called by: $this->getAdminLogsPageTable($sub)
             ├── call name: getAdminLogsPageTable
             └── in line: Some(1854)
    in method: function echoAdminLogsPage(this)
             └── in file: includes/View.php
    called by: $abj404view->echoAdminLogsPage()
             ├── call name: echoAdminLogsPage
             └── in line: Some(117)
    in method: function echoChosenAdminTab(this,$action,$sub,$message)
             └── in file: includes/View.php
    called by: $abj404view->echoChosenAdminTab($action,$sub,$message)
             ├── call name: echoChosenAdminTab
             └── in line: Some(76)
        found: is_admin()
             ├── in method: STATIC function handleMainAdminPageActionAndDisplay()
             └──── in file: includes/View.php

val res12:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/

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
    due_to(cpg, cpg.call.filter(_.code.contains("savePagebuilderSettings")), get_calls_for_methods(cpg, cpg.method.name("ampforwp_include_options_file")))
)
/* RESULT AS OF 11.07.2025 17:50

joern> due_to_is_admin(cpg, cpg.call.filter(_.code.contains("savePagebuilderSettings")))
Path 1
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-post-template-functions.php"
             ├── call name: require_once
             └── in line: Some(73)
    in method: function amp_add_post_template_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_add_post_template_actions()
             ├── call name: amp_add_post_template_actions
             └── in line: Some(93)
    in method: function amp_render()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("template_redirect","AMPforWP\\AMPVendor\\amp_render")
             ├── call name: add_action
             └── in line: Some(78)
    in method: function amp_prepare_render()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_prepare_render()
             ├── call name: amp_prepare_render
             └── in line: Some(51)
    in method: function amp_maybe_add_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: remove_action("wp","amp_maybe_add_actions")
             ├── call name: remove_action
             └── in line: Some(740)
    in method: function ampforwp_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("pre_amp_render_post","ampforwp_initialise_classes")
             ├── call name: add_action
             └── in line: Some(1223)
        found: is_admin()
             ├── in method: VIRTUAL PUBLIC STATIC function <global>()
             └──── in file: accelerated-moblie-pages.php

Path 2
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP_PAGE_BUILDER . "functions.php"
             ├── call name: require_once
             └── in line: Some(376)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/amp-page-builder.php
    called by: require_once AMPFORWP_PLUGIN_DIR . "pagebuilder/amp-page-builder.php"
             ├── call name: require_once
             └── in line: Some(834)
        found: is_admin()
             ├── in method: VIRTUAL PUBLIC STATIC function <global>()
             └──── in file: accelerated-moblie-pages.php

Path 3
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-helper-functions.php"
             ├── call name: require_once
             └── in line: Some(682)
    in method: function ampforwp_bundle_core_amp_files()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("plugins_loaded","ampforwp_bundle_core_amp_files",8)
             ├── call name: add_action
             └── in line: Some(704)
        found: is_admin()
             ├── in method: VIRTUAL PUBLIC STATIC function <global>()
             └──── in file: accelerated-moblie-pages.php

Path 4
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP_PAGE_BUILDER . "functions.php"
             ├── call name: require_once
             └── in line: Some(376)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/amp-page-builder.php
    called by: require_once AMPFORWP_PLUGIN_DIR . "pagebuilder/amp-page-builder.php"
             ├── call name: require_once
             └── in line: Some(834)
        found: is_admin()
             ├── in method: VIRTUAL PUBLIC STATIC function <global>()
             └──── in file: accelerated-moblie-pages.php

Path 5
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-post-template-functions.php"
             ├── call name: require_once
             └── in line: Some(73)
    in method: function amp_add_post_template_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_add_post_template_actions()
             ├── call name: amp_add_post_template_actions
             └── in line: Some(93)
    in method: function amp_render()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("template_redirect","AMPforWP\\AMPVendor\\amp_render")
             ├── call name: add_action
             └── in line: Some(78)
    in method: function amp_prepare_render()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_prepare_render()
             ├── call name: amp_prepare_render
             └── in line: Some(51)
    in method: function amp_maybe_add_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: remove_action("wp","amp_maybe_add_actions")
             ├── call name: remove_action
             └── in line: Some(740)
    in method: function ampforwp_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("pre_amp_render_post","ampforwp_initialise_classes")
             ├── call name: add_action
             └── in line: Some(1223)
        found: is_admin()
             ├── in method: VIRTUAL PUBLIC STATIC function <global>()
             └──── in file: accelerated-moblie-pages.php

Path 6
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-post-template-functions.php"
             ├── call name: require_once
             └── in line: Some(73)
    in method: function amp_add_post_template_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_add_post_template_actions()
             ├── call name: amp_add_post_template_actions
             └── in line: Some(93)
    in method: function amp_render()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("template_redirect","AMPforWP\\AMPVendor\\amp_render")
             ├── call name: add_action
             └── in line: Some(78)
    in method: function amp_prepare_render()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_prepare_render()
             ├── call name: amp_prepare_render
             └── in line: Some(51)
    in method: function amp_maybe_add_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: remove_action("wp","amp_maybe_add_actions")
             ├── call name: remove_action
             └── in line: Some(740)
    in method: function ampforwp_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("init","ampforwp_init")
             ├── call name: add_action
             └── in line: Some(707)
        found: is_admin()
             ├── in method: VIRTUAL PUBLIC STATIC function <global>()
             └──── in file: accelerated-moblie-pages.php

Path 7
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMPFORWP_PLUGIN_DIR . "includes/features/performance/performance-functions.php"
             ├── call name: require_once
             └── in line: Some(638)
    in method: PUBLIC function __construct(this)
             └── in file: accelerated-moblie-pages.php
    called by: Ampforwp_Init.__construct()
             ├── call name: __construct
             └── in line: Some(653)
    in method: function ampforwp_plugin_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("init","ampforwp_plugin_init",9)
             ├── call name: add_action
             └── in line: Some(656)
        found: is_admin()
             ├── in method: VIRTUAL PUBLIC STATIC function <global>()
             └──── in file: accelerated-moblie-pages.php

val res3:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator

joern> due_to_is_admin(cpg, cpg.call.filter(_.code.contains("Enter HTML in Body (beginning of body tag)")))
Path 1
        start: $includes/options/admin-config.php:<global>@tmp-301["title"] = esc_html__("Enter HTML in Body (beginning of body tag) ","accelerated-mobile-pages")
             ├── call name: <operator>.assignment
             └── in line: Some(3387)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: includes/options/admin-config.php
    called by: require_once dirname(__FILE__) . "/includes/options/admin-config.php"
             ├── call name: require_once
             └── in line: Some(537)
        found: is_admin()
             ├── in method: function ampforwp_include_options_file()
             └──── in file: accelerated-moblie-pages.php

val res4:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/
/* RESULT AS OF 11.07.2025 18:04

joern> due_to(cpg, cpg.call.filter(_.code.contains("savePagebuilderSettings")), get_calls_for_methods(cpg, cpg.method.name("ampforwp_include_options_file")))
Path 1
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-helper-functions.php"
             ├── call name: require_once
             └── in line: Some(682)
    in method: function ampforwp_bundle_core_amp_files()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("plugins_loaded","ampforwp_bundle_core_amp_files",8)
             ├── call name: add_action
             └── in line: Some(704)
        found: add_action("after_setup_theme","ampforwp_include_options_file")
             ├── in line: Some(532)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 2
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMPFORWP_PLUGIN_DIR . "includes/features/structure-data/structured-data-functions.php"
             ├── call name: require_once
             └── in line: Some(640)
    in method: PUBLIC function __construct(this)
             └── in file: accelerated-moblie-pages.php
    called by: Ampforwp_Init.__construct()
             ├── call name: __construct
             └── in line: Some(653)
    in method: function ampforwp_plugin_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("init","ampforwp_plugin_init",9)
             ├── call name: add_action
             └── in line: Some(656)
        found: add_action("after_setup_theme","ampforwp_include_options_file")
             ├── in line: Some(532)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 3
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-post-template-functions.php"
             ├── call name: require_once
             └── in line: Some(73)
    in method: function amp_add_post_template_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_add_post_template_actions()
             ├── call name: amp_add_post_template_actions
             └── in line: Some(93)
    in method: function amp_render()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("template_redirect","AMPforWP\\AMPVendor\\amp_render")
             ├── call name: add_action
             └── in line: Some(78)
    in method: function amp_prepare_render()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_prepare_render()
             ├── call name: amp_prepare_render
             └── in line: Some(51)
    in method: function amp_maybe_add_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("wp","AMPforWP\\AMPVendor\\amp_maybe_add_actions")
             ├── call name: add_action
             └── in line: Some(728)
    in method: function ampforwp_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("pre_amp_render_post","ampforwp_initialise_classes")
             ├── call name: add_action
             └── in line: Some(1223)
        found: add_action("after_setup_theme","ampforwp_include_options_file")
             ├── in line: Some(532)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 4
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP_PAGE_BUILDER . "functions.php"
             ├── call name: require_once
             └── in line: Some(376)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/amp-page-builder.php
    called by: require_once AMPFORWP_PLUGIN_DIR . "pagebuilder/amp-page-builder.php"
             ├── call name: require_once
             └── in line: Some(834)
        found: add_action("after_setup_theme","ampforwp_include_options_file")
             ├── in line: Some(532)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 5
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-post-template-functions.php"
             ├── call name: require_once
             └── in line: Some(73)
    in method: function amp_add_post_template_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_add_post_template_actions()
             ├── call name: amp_add_post_template_actions
             └── in line: Some(93)
    in method: function amp_render()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("template_redirect","AMPforWP\\AMPVendor\\amp_render")
             ├── call name: add_action
             └── in line: Some(78)
    in method: function amp_prepare_render()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_prepare_render()
             ├── call name: amp_prepare_render
             └── in line: Some(51)
    in method: function amp_maybe_add_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("wp","AMPforWP\\AMPVendor\\amp_maybe_add_actions")
             ├── call name: add_action
             └── in line: Some(728)
    in method: function ampforwp_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("init","ampforwp_init")
             ├── call name: add_action
             └── in line: Some(707)
        found: add_action("after_setup_theme","ampforwp_include_options_file")
             ├── in line: Some(532)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

val res5:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/

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
    due_to(cpg, cpg.call.filter(_.code.contains("savePagebuilderSettings")), get_calls_for_methods(cpg, cpg.method.name("ampforwp_include_options_file")))
)
/* RESULT AS OF 11.07.2025 18:50

joern> due_to_is_admin(cpg, cpg.call.filter(_.code.contains("savePagebuilderSettings")))
Path 1
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-post-template-functions.php"
             ├── call name: require_once
             └── in line: Some(73)
    in method: function amp_add_post_template_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_add_post_template_actions()
             ├── call name: amp_add_post_template_actions
             └── in line: Some(93)
    in method: function amp_render()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("template_redirect","AMPforWP\\AMPVendor\\amp_render")
             ├── call name: add_action
             └── in line: Some(78)
    in method: function amp_prepare_render()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_prepare_render()
             ├── call name: amp_prepare_render
             └── in line: Some(51)
    in method: function amp_maybe_add_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("wp","AMPforWP\\AMPVendor\\amp_maybe_add_actions")
             ├── call name: add_action
             └── in line: Some(728)
    in method: function ampforwp_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("init","ampforwp_init")
             ├── call name: add_action
             └── in line: Some(707)
        found: is_admin()
             ├── in line: Some(563)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 2
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-post-template-functions.php"
             ├── call name: require_once
             └── in line: Some(73)
    in method: function amp_add_post_template_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_add_post_template_actions()
             ├── call name: amp_add_post_template_actions
             └── in line: Some(93)
    in method: function amp_render()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("template_redirect","AMPforWP\\AMPVendor\\amp_render")
             ├── call name: add_action
             └── in line: Some(78)
    in method: function amp_prepare_render()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_prepare_render()
             ├── call name: amp_prepare_render
             └── in line: Some(51)
    in method: function amp_maybe_add_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("wp","AMPforWP\\AMPVendor\\amp_maybe_add_actions")
             ├── call name: add_action
             └── in line: Some(728)
    in method: function ampforwp_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("pre_amp_render_post","ampforwp_initialise_classes")
             ├── call name: add_action
             └── in line: Some(1223)
        found: is_admin()
             ├── in line: Some(563)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 3
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP_PAGE_BUILDER . "functions.php"
             ├── call name: require_once
             └── in line: Some(376)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/amp-page-builder.php
    called by: require_once AMPFORWP_PLUGIN_DIR . "pagebuilder/amp-page-builder.php"
             ├── call name: require_once
             └── in line: Some(834)
        found: is_admin()
             ├── in line: Some(563)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 4
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMPFORWP_PLUGIN_DIR . "includes/features/structure-data/structured-data-functions.php"
             ├── call name: require_once
             └── in line: Some(640)
    in method: PUBLIC function __construct(this)
             └── in file: accelerated-moblie-pages.php
    called by: Ampforwp_Init.__construct()
             ├── call name: __construct
             └── in line: Some(653)
    in method: function ampforwp_plugin_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("init","ampforwp_plugin_init",9)
             ├── call name: add_action
             └── in line: Some(656)
        found: is_admin()
             ├── in line: Some(563)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 5
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-post-template-functions.php"
             ├── call name: require_once
             └── in line: Some(73)
    in method: function amp_add_post_template_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_add_post_template_actions()
             ├── call name: amp_add_post_template_actions
             └── in line: Some(93)
    in method: function amp_render()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("template_redirect","AMPforWP\\AMPVendor\\amp_render")
             ├── call name: add_action
             └── in line: Some(78)
    in method: function amp_prepare_render()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_prepare_render()
             ├── call name: amp_prepare_render
             └── in line: Some(51)
    in method: function amp_maybe_add_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("wp","AMPforWP\\AMPVendor\\amp_maybe_add_actions")
             ├── call name: add_action
             └── in line: Some(728)
    in method: function ampforwp_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("pre_amp_render_post","ampforwp_initialise_classes")
             ├── call name: add_action
             └── in line: Some(1223)
        found: is_admin()
             ├── in line: Some(837)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 6
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP_PAGE_BUILDER . "functions.php"
             ├── call name: require_once
             └── in line: Some(376)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/amp-page-builder.php
    called by: require_once AMPFORWP_PLUGIN_DIR . "pagebuilder/amp-page-builder.php"
             ├── call name: require_once
             └── in line: Some(834)
        found: is_admin()
             ├── in line: Some(815)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 7
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-helper-functions.php"
             ├── call name: require_once
             └── in line: Some(682)
    in method: function ampforwp_bundle_core_amp_files()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("plugins_loaded","ampforwp_bundle_core_amp_files",8)
             ├── call name: add_action
             └── in line: Some(704)
        found: is_admin()
             ├── in line: Some(563)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

val res6:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator

joern> due_to_is_admin(cpg, cpg.call.filter(_.code.contains("Enter HTML in Body (beginning of body tag)")))
Path 1
        start: $includes/options/admin-config.php:<global>@tmp-301["title"] = esc_html__("Enter HTML in Body (beginning of body tag) ","accelerated-mobile-pages")
             ├── call name: <operator>.assignment
             └── in line: Some(3387)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: includes/options/admin-config.php
    called by: require_once dirname(__FILE__) . "/includes/options/admin-config.php"
             ├── call name: require_once
             └── in line: Some(537)
        found: is_admin()
             ├── in line: Some(535)
             ├──── in method: function ampforwp_include_options_file()
             └────── in file: accelerated-moblie-pages.php

val res7:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator

joern> due_to(cpg, cpg.call.filter(_.code.contains("savePagebuilderSettings")), get_calls_for_methods(cpg, cpg.method.name("ampforwp_include_options_file")))
Path 1
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-helper-functions.php"
             ├── call name: require_once
             └── in line: Some(682)
    in method: function ampforwp_bundle_core_amp_files()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("plugins_loaded","ampforwp_bundle_core_amp_files",8)
             ├── call name: add_action
             └── in line: Some(704)
        found: add_action("after_setup_theme","ampforwp_include_options_file")
             ├── in line: Some(532)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 2
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMPFORWP_PLUGIN_DIR . "includes/features/structure-data/structured-data-functions.php"
             ├── call name: require_once
             └── in line: Some(640)
    in method: PUBLIC function __construct(this)
             └── in file: accelerated-moblie-pages.php
    called by: Ampforwp_Init.__construct()
             ├── call name: __construct
             └── in line: Some(653)
    in method: function ampforwp_plugin_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("init","ampforwp_plugin_init",9)
             ├── call name: add_action
             └── in line: Some(656)
        found: add_action("after_setup_theme","ampforwp_include_options_file")
             ├── in line: Some(532)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 3
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-post-template-functions.php"
             ├── call name: require_once
             └── in line: Some(73)
    in method: function amp_add_post_template_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_add_post_template_actions()
             ├── call name: amp_add_post_template_actions
             └── in line: Some(93)
    in method: function amp_render()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("template_redirect","AMPforWP\\AMPVendor\\amp_render")
             ├── call name: add_action
             └── in line: Some(78)
    in method: function amp_prepare_render()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_prepare_render()
             ├── call name: amp_prepare_render
             └── in line: Some(51)
    in method: function amp_maybe_add_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("wp","AMPforWP\\AMPVendor\\amp_maybe_add_actions")
             ├── call name: add_action
             └── in line: Some(728)
    in method: function ampforwp_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("pre_amp_render_post","ampforwp_initialise_classes")
             ├── call name: add_action
             └── in line: Some(1223)
        found: add_action("after_setup_theme","ampforwp_include_options_file")
             ├── in line: Some(532)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 4
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP_PAGE_BUILDER . "functions.php"
             ├── call name: require_once
             └── in line: Some(376)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/amp-page-builder.php
    called by: require_once AMPFORWP_PLUGIN_DIR . "pagebuilder/amp-page-builder.php"
             ├── call name: require_once
             └── in line: Some(834)
        found: add_action("after_setup_theme","ampforwp_include_options_file")
             ├── in line: Some(532)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

Path 5
        start: echo "\" target=\"amppb-panels-export-iframe\"  method=\"post\">\n                                            <label class=\"import-export-label\">Export Current Layout</label>\n                                            <button type=\"submit\" class=\"button button-primary button-large\">\n                                                Export\n                                            </button>\n\n                                            <input type=\"hidden\" name=\"export_layout_data\" v-model=\"JSON.stringify(currentLayoutData)\" />\n                                        </form>\n                                        \n                                    </div>\n\n                                </div>\n                            </div><!-- export Closed-->\n                        </div>\n                        <div class=\"clearfix\"></div>\n                    </div>\n\n                    <div class=\"modal-footer\">\n                        <slot name=\"footer\">\n                            <span class=\"button button-primary button-large  del-btn-modal\" @click=\"loadLayOutFolder()\" v-if=\"innerLayouts!=\'\'\">\n                                Back\n                            </span>\n                            <button type=\"button\"  class=\"button modal-default-button\" v-if=\"modalCrrentTab==\'advance\'\" @click=\"savePagebuilderSettings(currentLayoutData)\">\n                                Save\n                            </button>\n                             <button type=\"button\"  class=\"button modal-default-button preview button\"  @click=\"hidePageBuilderPopUp()\">\n                                Close\n                            </button>\n                        </slot>\n                    </div>\n\n                </div>\n            </div>\n        </div>\n    </transition>\n</script>"
             ├── call name: echo
             └── in line: Some(176)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/components/pbSettingTemplates.php
    called by: include_once AMP_PAGE_BUILDER . "/components/pbSettingTemplates.php"
             ├── call name: include_once
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/inc/js-templates.php
    called by: include plugin_dir_path(__FILE__) . "/inc/js-templates.php"
             ├── call name: include
             └── in line: Some(117)
    in method: function amppb_js_templates()
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_footer","amppb_js_templates",9999)
             ├── call name: add_action
             └── in line: Some(108)
    in method: function amppbbase_admin_scripts($hook_suffix)
             └── in file: pagebuilder/functions.php
    called by: add_action("admin_enqueue_scripts","amppbbase_admin_scripts")
             ├── call name: add_action
             └── in line: Some(11)
    in method: VIRTUAL PUBLIC STATIC function <global>()
             └── in file: pagebuilder/functions.php
    called by: require_once AMP__VENDOR__DIR__ . "/includes/amp-post-template-functions.php"
             ├── call name: require_once
             └── in line: Some(73)
    in method: function amp_add_post_template_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_add_post_template_actions()
             ├── call name: amp_add_post_template_actions
             └── in line: Some(93)
    in method: function amp_render()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("template_redirect","AMPforWP\\AMPVendor\\amp_render")
             ├── call name: add_action
             └── in line: Some(78)
    in method: function amp_prepare_render()
             └── in file: includes/vendor/amp/amp.php
    called by: amp_prepare_render()
             ├── call name: amp_prepare_render
             └── in line: Some(51)
    in method: function amp_maybe_add_actions()
             └── in file: includes/vendor/amp/amp.php
    called by: add_action("wp","AMPforWP\\AMPVendor\\amp_maybe_add_actions")
             ├── call name: add_action
             └── in line: Some(728)
    in method: function ampforwp_init()
             └── in file: accelerated-moblie-pages.php
    called by: add_action("init","ampforwp_init")
             ├── call name: add_action
             └── in line: Some(707)
        found: add_action("after_setup_theme","ampforwp_include_options_file")
             ├── in line: Some(532)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: accelerated-moblie-pages.php

val res8:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/

/* accordions-or-faqs.2.0.3

SINKS
Includes/Settings.php:100
└── input element with `id="accordions_or_faqs_license_key"`
*/
def plugin_06 = due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("accordions_or_faqs_license_key")))
/* RESULT AS OF 11.07.2025 18:59

joern> due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("accordions_or_faqs_license_key")))
Path 1
        start: echo ">No\r\n                                        </label>\r\n                                        <span class=\"oxi-addons-settings-connfirmation oxi_addons_font_awesome\"></span>\r\n                                        <br>\r\n                                        <p class=\"description\">Load Font Awesome CSS at shortcode loading, If your theme already loaded select No for faster loading</p>\r\n                                    </fieldset>\r\n                                </td>\r\n                            </tr>\r\n\r\n                        </tbody>\r\n                    </table>\r\n                    <br>\r\n                    <br>\r\n\r\n                    <h2>Product License</h2>\r\n                    <table class=\"form-table\" role=\"presentation\">\r\n                        <tbody>\r\n                            <tr>\r\n                                <th scope=\"row\">\r\n                                    <label for=\"accordions_or_faqs_license_key\">License Key</label>\r\n                                </th>\r\n                                <td class=\"valid\">\r\n                                    <input type=\"text\" class=\"regular-text\" id=\"accordions_or_faqs_license_key\" name=\"accordions_or_faqs_license_key\" value=\""
             ├── call name: echo
             └── in line: Some(78)
    in method: PUBLIC function Render(this)
             └── in file: Includes/Settings.php
    called by: $this->Render()
             ├── call name: Render
             └── in line: Some(30)
    in method: PUBLIC function __construct(this)
             └── in file: Includes/Settings.php
    called by: OXI_ACCORDIONS_PLUGINS\Includes\Settings.__construct()
             ├── call name: __construct
             └── in line: Some(205)
    in method: PUBLIC function user_settings(this)
             └── in file: Helper/Helper.php
    called by: add_submenu_page("oxi-accordions-ultimate","Settings","Settings",$first_key,"oxi-accordions-ultimate-settings",)
             ├── call name: add_submenu_page
             └── in line: Some(86)
    in method: PUBLIC function admin_menu(this)
             └── in file: Helper/Helper.php
    called by: add_action("admin_menu",)
             ├── call name: add_action
             └── in line: Some(66)
    in method: PUBLIC function User_Admin(this)
             └── in file: Classes/Bootstrap.php
    called by: $this->User_Admin()
             ├── call name: User_Admin
             └── in line: Some(37)
        found: is_admin()
             ├── in line: Some(36)
             ├──── in method: PUBLIC function __construct(this)
             └────── in file: Classes/Bootstrap.php

val res5:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/

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
def plugin_07 = due_to_is_admin(cpg, get_calls_for_methods(cpg, cpg.method.fullName("ACOPLW_Backend<metaclass>.view")))
/* RESULT AS OF 11.07.2025 20:09

joern> due_to_is_admin(cpg, get_calls_for_methods(cpg, cpg.method.fullName("ACOPLW_Backend<metaclass>.view")))
Path 1
        start: ACOPLW_Backend::view("admin-root",)
             ├── call name: view
             └── in line: Some(195)
    in method: PUBLIC function admin_ui(this)
             └── in file: includes/class-acoplw-backend.php
    called by: add_menu_page(__("Badges","aco-product-labels-for-woocommerce"),__("Badges","aco-product-labels-for-woocommerce"),"edit_products","acoplw_badges_ui",,esc_url($this->assets_url) . "/images/icon.png",25)
             ├── call name: add_menu_page
             └── in line: Some(185)
    in method: PUBLIC function register_root_page(this)
             └── in file: includes/class-acoplw-backend.php
    called by: add_action("admin_menu",)
             ├── call name: add_action
             └── in line: Some(101)
    in method: PUBLIC function __construct(this,$file,$version)
             └── in file: includes/class-acoplw-backend.php
    called by: self.__construct($file,$version)
             ├── call name: __construct
             └── in line: Some(178)
    in method: PUBLIC STATIC function instance($file,$version)
             └── in file: includes/class-acoplw-backend.php
    called by: ACOPLW_Backend::instance(__FILE__,ACOPLW_VERSION)
             ├── call name: instance
             └── in line: Some(60)
    in method: function ACOPLW()
             └── in file: start.php
    called by: ACOPLW()
             ├── call name: ACOPLW
             └── in line: Some(68)
        found: is_admin()
             ├── in line: Some(67)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: start.php

Path 2
        start: ACOPLW_Backend::view("admin-lists",)
             ├── call name: view
             └── in line: Some(223)
    in method: PUBLIC function admin_ui_pro_lists(this)
             └── in file: includes/class-acoplw-backend.php
    called by: add_submenu_page("acoplw_badges_ui",__("Product Lists","aco-product-labels-for-woocommerce"),__("Product Lists","aco-product-labels-for-woocommerce"),"edit_products","acoplw_product_lists_ui",)
             ├── call name: add_submenu_page
             └── in line: Some(187)
    in method: PUBLIC function register_root_page(this)
             └── in file: includes/class-acoplw-backend.php
    called by: add_action("admin_menu",)
             ├── call name: add_action
             └── in line: Some(101)
    in method: PUBLIC function __construct(this,$file,$version)
             └── in file: includes/class-acoplw-backend.php
    called by: self.__construct($file,$version)
             ├── call name: __construct
             └── in line: Some(178)
    in method: PUBLIC STATIC function instance($file,$version)
             └── in file: includes/class-acoplw-backend.php
    called by: ACOPLW_Backend::instance(__FILE__,ACOPLW_VERSION)
             ├── call name: instance
             └── in line: Some(60)
    in method: function ACOPLW()
             └── in file: start.php
    called by: ACOPLW()
             ├── call name: ACOPLW
             └── in line: Some(68)
        found: is_admin()
             ├── in line: Some(67)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: start.php

Path 3
        start: ACOPLW_Backend::view("admin-settings",)
             ├── call name: view
             └── in line: Some(228)
    in method: PUBLIC function admin_ui_settings(this)
             └── in file: includes/class-acoplw-backend.php
    called by: add_submenu_page("acoplw_badges_ui",__("Settings","aco-product-labels-for-woocommerce"),__("Settings","aco-product-labels-for-woocommerce"),"edit_products","acoplw_settings_ui",)
             ├── call name: add_submenu_page
             └── in line: Some(189)
    in method: PUBLIC function register_root_page(this)
             └── in file: includes/class-acoplw-backend.php
    called by: add_action("admin_menu",)
             ├── call name: add_action
             └── in line: Some(101)
    in method: PUBLIC function __construct(this,$file,$version)
             └── in file: includes/class-acoplw-backend.php
    called by: self.__construct($file,$version)
             ├── call name: __construct
             └── in line: Some(178)
    in method: PUBLIC STATIC function instance($file,$version)
             └── in file: includes/class-acoplw-backend.php
    called by: ACOPLW_Backend::instance(__FILE__,ACOPLW_VERSION)
             ├── call name: instance
             └── in line: Some(60)
    in method: function ACOPLW()
             └── in file: start.php
    called by: ACOPLW()
             ├── call name: ACOPLW
             └── in line: Some(68)
        found: is_admin()
             ├── in line: Some(67)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: start.php

val res1:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/

/* add-to-any.1.7.45

SINKS
addtoany.admin.php
└── submit button (line 874) submits form with unsanitised input (line 569)
*/
def plugin_08 = List(
    due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("A2A_SHARE_SAVE_header"))),
    due_to_is_admin(cpg,cpg.call.name("echo").filter(_.code.contains("""<input class=\"button-primary\" type=\"submit\" name=\"Submit\" value=\"""")))
)
/* RESULT AS OF 11.07.2025 20:32

joern> due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("A2A_SHARE_SAVE_header")))
Path 1
        start: echo "\" role=\"region\">\n\t\t\t\t<label>\n\t\t\t\t\t<input name=\"A2A_SHARE_SAVE_header\" type=\"text\" class=\"code\" placeholder=\""
             ├── call name: echo
             └── in line: Some(567)
    in method: function A2A_SHARE_SAVE_options_page()
             └── in file: addtoany.admin.php
    called by: add_options_page(__("AddToAny Share Settings","add-to-any"),__("AddToAny","add-to-any"),"manage_options","addtoany","A2A_SHARE_SAVE_options_page")
             ├── call name: add_options_page
             └── in line: Some(1224)
    in method: function A2A_SHARE_SAVE_add_menu_link()
             └── in file: add-to-any.php
    called by: add_filter("admin_menu","A2A_SHARE_SAVE_add_menu_link")
             ├── call name: add_filter
             └── in line: Some(1233)
        found: is_admin()
             ├── in line: Some(1219)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: add-to-any.php

val res8:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator

joern> due_to_is_admin(cpg,cpg.call.name("echo").filter(_.code.contains("""<input class=\"button-primary\" type=\"submit\" name=\"Submit\" value=\"""")))
Path 1
        start: echo "\t\t\n\t\t</table>\n\t\t\n\t\t<p class=\"submit\">\n\t\t\t<input class=\"button-primary\" type=\"submit\" name=\"Submit\" value=\""
             ├── call name: echo
             └── in line: Some(870)
    in method: function A2A_SHARE_SAVE_options_page()
             └── in file: addtoany.admin.php
    called by: add_options_page(__("AddToAny Share Settings","add-to-any"),__("AddToAny","add-to-any"),"manage_options","addtoany","A2A_SHARE_SAVE_options_page")
             ├── call name: add_options_page
             └── in line: Some(1224)
    in method: function A2A_SHARE_SAVE_add_menu_link()
             └── in file: add-to-any.php
    called by: add_filter("admin_menu","A2A_SHARE_SAVE_add_menu_link")
             ├── call name: add_filter
             └── in line: Some(1233)
        found: is_admin()
             ├── in line: Some(1219)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: add-to-any.php

val res9:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/

/* add-to-any.1.7.47

SINKS
addtoany.admin.php
└── submit button (line 874) submits form with unsanitised input (line 563)
*/
def plugin_09 = List(
    due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("A2A_SHARE_SAVE_button_custom"))),
    due_to_is_admin(cpg,cpg.call.name("echo").filter(_.code.contains("""<input class=\"button-primary\" type=\"submit\" name=\"Submit\" value=\"""")))
)
/* RESULT AS OF 11.07.2025 20:40

joern> due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("A2A_SHARE_SAVE_button_custom")))
Path 1
        start: echo ":</span>\n\t\t\t\t\t</label>\n\t\t\t\t\t<input name=\"A2A_SHARE_SAVE_button_custom\" type=\"text\" class=\"code\" size=\"50\" onclick=\"document.getElementById(\'A2A_SHARE_SAVE_button_is_custom\').checked=true\" style=\"vertical-align:middle\" value=\""
             ├── call name: echo
             └── in line: Some(561)
    in method: function A2A_SHARE_SAVE_options_page()
             └── in file: addtoany.admin.php
    called by: add_options_page(__("AddToAny Share Settings","add-to-any"),__("AddToAny","add-to-any"),"manage_options","addtoany","A2A_SHARE_SAVE_options_page")
             ├── call name: add_options_page
             └── in line: Some(1224)
    in method: function A2A_SHARE_SAVE_add_menu_link()
             └── in file: add-to-any.php
    called by: add_filter("admin_menu","A2A_SHARE_SAVE_add_menu_link")
             ├── call name: add_filter
             └── in line: Some(1233)
        found: is_admin()
             ├── in line: Some(1219)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: add-to-any.php

val res12:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/

/* add-to-feedly.1.2.11

SINKS
addtofeedly.php
└── `submit_button()` (line 80) submits form with unsanitised input (line 55)
*/
def plugin_10 = due_to_is_admin(cpg, cpg.call.name("submit_button"))
/* RESULT AS OF

joern> due_to_is_admin(cpg, cpg.call.name("submit_button"))
Path 1
        start: submit_button()
             ├── call name: submit_button
             └── in line: Some(80)
    in method: function ADD_TO_FEEDLY_settings_page()
             └── in file: addtofeedly.php
    called by: add_menu_page("Add to Feedly Plugin","Add to Feedly","administrator",__FILE__,"ADD_TO_FEEDLY_settings_page",plugins_url("/images/icon.png",__FILE__))
             ├── call name: add_menu_page
             └── in line: Some(16)
    in method: function ADD_TO_FEEDLY_create_menu()
             └── in file: addtofeedly.php
    called by: add_action("admin_menu","ADD_TO_FEEDLY_create_menu")
             ├── call name: add_action
             └── in line: Some(209)
        found: is_admin()
             ├── in line: Some(208)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: addtofeedly.php

val res15:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/

/* add-whatsapp-button.2.1.7

SINKS
admin/settings.php:335
└── uses `sanitize_text_field()` to set content of `value` attribute
*/
def plugin_11 = due_to_is_admin(cpg, cpg.call.name("sanitize_text_field"))
/* RESULT AS OF 11.07.2025 20:50

joern> due_to_is_admin(cpg, cpg.call.name("sanitize_text_field"))
Path 1
        start: sanitize_text_field($settings["icon_size"])
             ├── call name: sanitize_text_field
             └── in line: Some(33)
    in method: PUBLIC STATIC function print_styles()
             └── in file: includes/style-templates/frontend-css.php
    called by: add_action("wp_head",)
             ├── call name: add_action
             └── in line: Some(19)
    in method: PUBLIC function __construct(this)
             └── in file: includes/styles-manager.php
    called by: Add_Chat_App_Button\Includes\Styles_Manager.__construct()
             ├── call name: __construct
             └── in line: Some(167)
    in method: PUBLIC function init(this)
             └── in file: plugin.php
    called by: add_action("admin_init",)
             ├── call name: add_action
             └── in line: Some(109)
    in method: PRIVATE function admin_actions(this)
             └── in file: plugin.php
    called by: $this->admin_actions()
             ├── call name: admin_actions
             └── in line: Some(66)
        found: is_admin()
             ├── in line: Some(65)
             ├──── in method: PUBLIC function __construct(this)
             └────── in file: plugin.php

Path 2
        start: sanitize_text_field($settings["icon_size"])
             ├── call name: sanitize_text_field
             └── in line: Some(163)
    in method: PUBLIC function print_button(this)
             └── in file: plugin.php
    called by: add_action("wp_footer",)
             ├── call name: add_action
             └── in line: Some(171)
    in method: PUBLIC function init(this)
             └── in file: plugin.php
    called by: add_action("admin_init",)
             ├── call name: add_action
             └── in line: Some(109)
    in method: PRIVATE function admin_actions(this)
             └── in file: plugin.php
    called by: $this->admin_actions()
             ├── call name: admin_actions
             └── in line: Some(66)
        found: is_admin()
             ├── in line: Some(65)
             ├──── in method: PUBLIC function __construct(this)
             └────── in file: plugin.php

Path 3
        start: sanitize_text_field($settings["button_text"])
             ├── call name: sanitize_text_field
             └── in line: Some(178)
    in method: PUBLIC STATIC function print_styles()
             └── in file: includes/style-templates/admin-css.php
    called by: add_action("admin_head",)
             ├── call name: add_action
             └── in line: Some(25)
        found: is_admin()
             ├── in line: Some(21)
             ├──── in method: PUBLIC function __construct(this)
             └────── in file: includes/styles-manager.php

Path 4
        start: sanitize_text_field($settings["icon_size"])
             ├── call name: sanitize_text_field
             └── in line: Some(33)
    in method: PUBLIC STATIC function print_styles()
             └── in file: includes/style-templates/frontend-css.php
    called by: add_action("admin_head",)
             ├── call name: add_action
             └── in line: Some(25)
        found: is_admin()
             ├── in line: Some(21)
             ├──── in method: PUBLIC function __construct(this)
             └────── in file: includes/styles-manager.php

Path 5
        start: sanitize_text_field($settings["distance_from_bottom"])
             ├── call name: sanitize_text_field
             └── in line: Some(335)
    in method: PUBLIC function print_options_page_content(this)
             └── in file: admin/settings.php
    called by: add_options_page(__("Add WhatsApp Button Options","add-whatsapp-button"),__("Add WhatsApp Button","add-whatsapp-button"),"manage_options","awb-options",)
             ├── call name: add_options_page
             └── in line: Some(29)
    in method: PUBLIC function options_menu_link(this)
             └── in file: admin/settings.php
    called by: add_action("admin_menu",)
             ├── call name: add_action
             └── in line: Some(16)
    in method: PUBLIC function __construct(this)
             └── in file: admin/settings.php
    called by: Add_Chat_App_Button\Admin\Admin_Settings.__construct()
             ├── call name: __construct
             └── in line: Some(116)
    in method: PRIVATE function admin_actions(this)
             └── in file: plugin.php
    called by: $this->admin_actions()
             ├── call name: admin_actions
             └── in line: Some(66)
        found: is_admin()
             ├── in line: Some(65)
             ├──── in method: PUBLIC function __construct(this)
             └────── in file: plugin.php

Path 6
        start: sanitize_text_field($settings["button_text_color"])
             ├── call name: sanitize_text_field
             └── in line: Some(328)
    in method: PUBLIC STATIC function dismiss_admin_notice()
             └── in file: vendors/persist-admin-notices-dismissal/persist-admin-notices-dismissal.php
    called by: add_action("wp_ajax_dismiss_admin_notice",)
             ├── call name: add_action
             └── in line: Some(47)
    in method: PUBLIC STATIC function init()
             └── in file: vendors/persist-admin-notices-dismissal/persist-admin-notices-dismissal.php
    called by: add_action("admin_init",)
             ├── call name: add_action
             └── in line: Some(109)
    in method: PRIVATE function admin_actions(this)
             └── in file: plugin.php
    called by: $this->admin_actions()
             ├── call name: admin_actions
             └── in line: Some(66)
        found: is_admin()
             ├── in line: Some(65)
             ├──── in method: PUBLIC function __construct(this)
             └────── in file: plugin.php

val res19:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/

/* ad-injection.1.2.0.19

SINKS
ui-tab-main.php
└── combined with ad-injection-admin.php
    └── `_e`-calls with `Save all settings` submit forms with unsanitised input (ui-tab-main.php:430)
*/
def plugin_12 = List(
    due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("""<textarea name=\"ad_code_top_1\""""))),
    due_to_is_admin(cpg, cpg.call.name("_e").filter(_.code.contains("Save all settings")))
)
/* RESULT AS OF 11.07.2025 21:05

joern> due_to_is_admin(cpg, cpg.call.name("echo").filter(_.code.contains("""<textarea name=\"ad_code_top_1\"""")))
Path 1
        start: echo "\t\r\n\t<h3><a name=\"topadcode\"></a>Top ad (below post title - this is not a \'header\' ad) [<a href=\"#topadplacement\">placement</a>] <!--[<a href=\'?page=ad-injection&amp;tab=adrotation#multiple_top\'>pool</a>]--></h3>\r\n\t<table border=\"0\" class=\"adinjtable\" width=\"98%\">\r\n\t<tr><td>\r\n\t<textarea name=\"ad_code_top_1\" rows=\"10\" cols=\""
             ├── call name: echo
             └── in line: Some(426)
    in method: function adinj_adverts_box($ops)
             └── in file: ui-tab-main.php
    called by: adinj_adverts_box($ops)
             ├── call name: adinj_adverts_box
             └── in line: Some(21)
    in method: function adinj_tab_main()
             └── in file: ui-tab-main.php
    called by: adinj_tab_main()
             ├── call name: adinj_tab_main
             └── in line: Some(291)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

val res25:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator

joern> due_to_is_admin(cpg, cpg.call.name("_e").filter(_.code.contains("Save all settings")))
Path 1
        start: _e("Save all settings","adinj")
             ├── call name: _e
             └── in line: Some(490)
    in method: function adinj_postbox_start($title,$anchor,$width)
             └── in file: ad-injection-admin.php
    called by: adinj_postbox_start(__("Ad placement settings","adinj"),"adsettings")
             ├── call name: adinj_postbox_start
             └── in line: Some(44)
    in method: function adinj_placement_settings_box($ops)
             └── in file: ui-tab-main.php
    called by: adinj_placement_settings_box($ops)
             ├── call name: adinj_placement_settings_box
             └── in line: Some(19)
    in method: function adinj_tab_main()
             └── in file: ui-tab-main.php
    called by: adinj_tab_main()
             ├── call name: adinj_tab_main
             └── in line: Some(291)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

Path 2
        start: _e("Save all settings","adinj")
             ├── call name: _e
             └── in line: Some(490)
    in method: function adinj_postbox_start($title,$anchor,$width)
             └── in file: ad-injection-admin.php
    called by: adinj_postbox_start(__("Top adverts","adinj"),"multiple_top")
             ├── call name: adinj_postbox_start
             └── in line: Some(44)
    in method: function adinj_tab_adrotation()
             └── in file: ui-tab-adrotation.php
    called by: adinj_tab_adrotation()
             ├── call name: adinj_tab_adrotation
             └── in line: Some(271)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

Path 3
        start: _e("Save all settings","adinj")
             ├── call name: _e
             └── in line: Some(490)
    in method: function adinj_postbox_start($title,$anchor,$width)
             └── in file: ad-injection-admin.php
    called by: adinj_postbox_start(__("Quick Start","adinj"),"docsquickstart","95%")
             ├── call name: adinj_postbox_start
             └── in line: Some(1000)
    in method: function adinj_docs()
             └── in file: ui-tab-main.php
    called by: adinj_docs()
             ├── call name: adinj_docs
             └── in line: Some(31)
    in method: function adinj_tab_main()
             └── in file: ui-tab-main.php
    called by: adinj_tab_main()
             ├── call name: adinj_tab_main
             └── in line: Some(291)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

Path 4
        start: _e("Save all settings","adinj")
             ├── call name: _e
             └── in line: Some(490)
    in method: function adinj_postbox_start($title,$anchor,$width)
             └── in file: ad-injection-admin.php
    called by: adinj_postbox_start(__("Test Adverts and template ad examples","adinj"),"testads","95%")
             ├── call name: adinj_postbox_start
             └── in line: Some(1065)
    in method: function adinj_testads()
             └── in file: ad-injection-admin.php
    called by: adinj_testads()
             ├── call name: adinj_testads
             └── in line: Some(33)
    in method: function adinj_tab_main()
             └── in file: ui-tab-main.php
    called by: adinj_tab_main()
             ├── call name: adinj_tab_main
             └── in line: Some(291)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

Path 5
        start: _e("Save all settings","adinj")
             ├── call name: _e
             └── in line: Some(369)
    in method: function adinj_placement_settings_box($ops)
             └── in file: ui-tab-main.php
    called by: adinj_placement_settings_box($ops)
             ├── call name: adinj_placement_settings_box
             └── in line: Some(19)
    in method: function adinj_tab_main()
             └── in file: ui-tab-main.php
    called by: adinj_tab_main()
             ├── call name: adinj_tab_main
             └── in line: Some(291)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

Path 6
        start: _e("Save all settings","adinj")
             ├── call name: _e
             └── in line: Some(148)
    in method: function adinj_placement_settings_box($ops)
             └── in file: ui-tab-main.php
    called by: adinj_placement_settings_box($ops)
             ├── call name: adinj_placement_settings_box
             └── in line: Some(19)
    in method: function adinj_tab_main()
             └── in file: ui-tab-main.php
    called by: adinj_tab_main()
             ├── call name: adinj_tab_main
             └── in line: Some(291)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

Path 7
        start: _e("Save all settings","adinj")
             ├── call name: _e
             └── in line: Some(490)
    in method: function adinj_postbox_start($title,$anchor,$width)
             └── in file: ad-injection-admin.php
    called by: adinj_postbox_start(__("Debugging","adinj"),"debugging")
             ├── call name: adinj_postbox_start
             └── in line: Some(12)
    in method: function adinj_tab_debug()
             └── in file: ui-tab-debug.php
    called by: adinj_tab_debug()
             ├── call name: adinj_tab_debug
             └── in line: Some(280)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

Path 8
        start: _e("Save all settings","adinj")
             ├── call name: _e
             └── in line: Some(490)
    in method: function adinj_postbox_start($title,$anchor,$width)
             └── in file: ad-injection-admin.php
    called by: adinj_postbox_start(__("Adverts","adinj"),"adverts")
             ├── call name: adinj_postbox_start
             └── in line: Some(425)
    in method: function adinj_adverts_box($ops)
             └── in file: ui-tab-main.php
    called by: adinj_adverts_box($ops)
             ├── call name: adinj_adverts_box
             └── in line: Some(21)
    in method: function adinj_tab_main()
             └── in file: ui-tab-main.php
    called by: adinj_tab_main()
             ├── call name: adinj_tab_main
             └── in line: Some(291)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

Path 9
        start: _e("Save all settings","adinj")
             ├── call name: _e
             └── in line: Some(490)
    in method: function adinj_postbox_start($title,$anchor,$width)
             └── in file: ad-injection-admin.php
    called by: adinj_postbox_start(__("Tag, category, author and post id filters","adinj"),"filters")
             ├── call name: adinj_postbox_start
             └── in line: Some(597)
    in method: function adinj_filters_box($ops)
             └── in file: ui-tab-main.php
    called by: adinj_filters_box($ops)
             ├── call name: adinj_filters_box
             └── in line: Some(25)
    in method: function adinj_tab_main()
             └── in file: ui-tab-main.php
    called by: adinj_tab_main()
             ├── call name: adinj_tab_main
             └── in line: Some(291)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

Path 10
        start: _e("Save all settings","adinj")
             ├── call name: _e
             └── in line: Some(490)
    in method: function adinj_postbox_start($title,$anchor,$width)
             └── in file: ad-injection-admin.php
    called by: adinj_postbox_start(__("Global settings (apply to all ads)","adinj"),"global")
             ├── call name: adinj_postbox_start
             └── in line: Some(496)
    in method: function adinj_global_settings_box($ops)
             └── in file: ui-tab-main.php
    called by: adinj_global_settings_box($ops)
             ├── call name: adinj_global_settings_box
             └── in line: Some(17)
    in method: function adinj_tab_main()
             └── in file: ui-tab-main.php
    called by: adinj_tab_main()
             ├── call name: adinj_tab_main
             └── in line: Some(291)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

Path 11
        start: _e("Save all settings","adinj")
             ├── call name: _e
             └── in line: Some(490)
    in method: function adinj_postbox_start($title,$anchor,$width)
             └── in file: ad-injection-admin.php
    called by: adinj_postbox_start(__("Ad insertion mode and dynamic ad display restrictions","adinj"),"restrictions")
             ├── call name: adinj_postbox_start
             └── in line: Some(616)
    in method: function adinj_insertion_mode_box($ops)
             └── in file: ui-tab-main.php
    called by: adinj_insertion_mode_box($ops)
             ├── call name: adinj_insertion_mode_box
             └── in line: Some(23)
    in method: function adinj_tab_main()
             └── in file: ui-tab-main.php
    called by: adinj_tab_main()
             ├── call name: adinj_tab_main
             └── in line: Some(291)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

Path 12
        start: _e("Save all settings","adinj")
             ├── call name: _e
             └── in line: Some(329)
    in method: function adinj_placement_settings_box($ops)
             └── in file: ui-tab-main.php
    called by: adinj_placement_settings_box($ops)
             ├── call name: adinj_placement_settings_box
             └── in line: Some(19)
    in method: function adinj_tab_main()
             └── in file: ui-tab-main.php
    called by: adinj_tab_main()
             ├── call name: adinj_tab_main
             └── in line: Some(291)
    in method: function adinj_options_page()
             └── in file: ad-injection-admin.php
    called by: add_options_page("Ad Injection","Ad Injection","manage_options",basename(__FILE__),"adinj_options_page")
             ├── call name: add_options_page
             └── in line: Some(73)
    in method: function adinj_admin_menu_hook()
             └── in file: ad-injection.php
    called by: add_action("admin_menu","adinj_admin_menu_hook")
             ├── call name: add_action
             └── in line: Some(1756)
        found: is_admin()
             ├── in line: Some(23)
             ├──── in method: VIRTUAL PUBLIC STATIC function <global>()
             └────── in file: ad-injection-admin.php

val res28:
  Iterator[List[? <: io.shiftleft.codepropertygraph.generated.nodes.AstNode]] = non-empty iterator
*/
