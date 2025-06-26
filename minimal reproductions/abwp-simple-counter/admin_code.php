<?php
/*
minimal reproduction of abwp-simple-counter/includes/admin-counters.php
*/

if (!class_exists('ABWP_simple_counter_admin')) {
    class ABWP_simple_counter_admin {
        public function __construct() {}
        public function view() {
            ob_start();
            include plugin_dir_path( __FILE__ ) . 'admin_page.php';
            $content = ob_get_clean();
            echo $content;
        }
        /*
        public function add_admin_help_tab() {}
        */
    }
}