<?php
/*
minimal reproduction of abwp-simple-counter/simple-counter.php
*/

if (!class_exists('ABWP_simple_counter')) {
    class ABWP_simple_counter {
        public function __construct() {
            if (is_admin()) {
                $this->load_dependencies();
                $this->define_admin_hooks();
            } else {
                add_action('init',array($this,'init'));
            }
        }
        private function load_dependencies() {
            require_once plugin_dir_path( __FILE__ ) . 'admin_code.php';
        }
        private function define_admin_hooks() {
            add_action('admin_menu',array($this,'admin_menu'));
            add_action('admin_init',array($this,'admin_init'));
            /* 
            add_action('plugins_loaded',array($this,'load_plugin_textdomain')); 
            */
        }
        /*
        public function load_plugin_textdomain() {}
        */
        public function admin_menu() {
            $admin_counters = new ABWP_simple_counter_admin();
            add_options_page(
                    __('Simple Counter', 'abwp-simple-counter'),
                    __('Simple Counter', 'abwp-simple-counter'),
                    'manage_options',
                    'simple-counter',
                    array($admin_counters, 'view') );
        }
        public function admin_init() {
			register_setting( 'abwp-simple-counter-options-group', 'abwp_sc_yandex_metrika');
			register_setting( 'abwp-simple-counter-options-group', 'abwp_sc_yandex_metrika_position');
            /* skipping other settings */
        }
        public function init() {
			add_action('wp_head', array($this, 'get_head_code'));
			add_action('wp_footer', array($this, 'get_footer_code'));
			add_shortcode('simple-counter', array($this, 'get_shortcode_counter') );
        }
        public function get_head_code() {
			if (get_option('abwp_sc_yandex_metrika_position') && (1 == get_option('abwp_sc_yandex_metrika_position'))) {
				if(get_option('abwp_sc_yandex_metrika')) {
					echo get_option('abwp_sc_yandex_metrika')."\n";
				}
			}
            /* skipping other options */
        }

		public function get_footer_code()
		{
			if (!get_option('abwp_sc_yandex_metrika_position') || (0 == get_option('abwp_sc_yandex_metrika_position'))) {
				if(get_option('abwp_sc_yandex_metrika')) {
					echo get_option('abwp_sc_yandex_metrika')."\n";
				}
			}
            /* skipping other options */
		}

		public function get_shortcode_counter($atts)
		{
			$return = '';
			switch ($atts['id']) {
				case 'metrika':
					if (get_option('abwp_sc_yandex_metrika_position') && (2 == get_option('abwp_sc_yandex_metrika_position'))) {
						if(get_option('abwp_sc_yandex_metrika')) {
							$return = get_option('abwp_sc_yandex_metrika')."\n";
						}
					}
					break;
                /* skipping other options (cases) */
				default:
					break;
			}
			return $return;
		}
    }
    $ABWP_simple_counter = new ABWP_simple_counter();
}