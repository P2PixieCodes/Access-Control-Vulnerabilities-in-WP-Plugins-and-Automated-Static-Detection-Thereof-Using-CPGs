<?php
/*
minimal reproduction of abwp-simple-counter/includes/page-admin-counters.php
*/
?>

<div>
    <h1>Title</h1>
    <form method="post" action="options.php">
        <?php settings_fields( 'abwp-simple-counter-options-group' ); ?>
        <table class="form-table">
            <!-- exploited input -->
			<tr valign="top">
				<th scope="row"><?php _e( 'Yandex.Metrica', 'abwp-simple-counter' ); ?></th>
				<td>
					<textarea name="abwp_sc_yandex_metrika" type="text" rows="10" class="large-text code" placeholder="<?php _e( 'HTML code counter', 'abwp-simple-counter' ); ?>"><?php echo get_option('abwp_sc_yandex_metrika'); ?></textarea>
					<p>
						<a href="https://metrika.yandex.ru/list/" target="_blank"><?php _e( 'My counters', 'abwp-simple-counter' ); ?></a> |
						<a href="https://metrika.yandex.ru/add/" target="_blank"><?php _e( 'Add counter', 'abwp-simple-counter' ); ?></a>
					</p>
				</td>
			</tr>

			<?php
				$yandex_metrika_position = get_option('abwp_sc_yandex_metrika_position');
				if (empty($yandex_metrika_position) || ((0 > $yandex_metrika_position) && (3 <= $yandex_metrika_position))) {
					$yandex_metrika_position = 0;
				}

			?>
			<tr valign="top">
				<th scope="row"><?php _e( 'Location counter', 'abwp-simple-counter' ); ?> <?php _e( 'Yandex.Metrica', 'abwp-simple-counter' ); ?></th>
				<td>
					<fieldset>
						<?php $checked = ((0 == $yandex_metrika_position)?' checked':''); ?>
						<label>
							<input type="radio" name="abwp_sc_yandex_metrika_position" value="0"<?php echo $checked;?>>
							<?php _e('Towards the end of the page', 'abwp-simple-counter' ); ?>
						</label><br>
						<?php $checked = ((1 == $yandex_metrika_position)?' checked':''); ?>
						<label><input type="radio" name="abwp_sc_yandex_metrika_position" value="1"<?php echo $checked;?>><?php _e('Closer to the top of the page', 'abwp-simple-counter' ); ?></label><br>
						<?php $checked = ((2 == $yandex_metrika_position)?' checked':''); ?>
						<label><input type="radio" name="abwp_sc_yandex_metrika_position" value="2"<?php echo $checked;?>><?php _e('Custom place', 'abwp-simple-counter' ); ?></label><br>
					</fieldset>
					<p>
						<?php _e('<i>Select the place of installation of the counter: at the beginning of page, end of page or in custom place. For installation in custom place use shortcode </i><code>[simple-counter id="metrika"]</code><i> or in the theme file of the website </i><code>&lt;?php echo do_shortcode(\'[simple-counter id="metrika"]\'); ?&gt;</code>', 'abwp-simple-counter' ); ?>
					</p>
				</td>
			</tr>
            <!-- skipping other input -->
        </table>
        <?php submit_button(); ?>
    </form>
</div>