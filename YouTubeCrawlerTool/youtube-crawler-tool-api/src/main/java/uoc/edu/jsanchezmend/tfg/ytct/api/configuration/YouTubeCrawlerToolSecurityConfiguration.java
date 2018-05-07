package uoc.edu.jsanchezmend.tfg.ytct.api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class YouTubeCrawlerToolSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private final static String USER_ROLE = "USER";
	
	@Value("${ytct.security.default.username}")
	private String defaultUsername;
	
	@Value("${ytct.security.default.password}")
	private String defaultPassword;
	

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            	// Crawler component security
                .antMatchers(HttpMethod.POST,	"/api/crawlers").authenticated()
                .antMatchers(HttpMethod.PUT,	"/api/crawlers/").authenticated()
                .antMatchers(HttpMethod.DELETE,	"/api/crawlers/").authenticated()
                // Video component security
                .antMatchers(HttpMethod.PUT,	"/api/videos/").authenticated()
                .antMatchers(HttpMethod.DELETE,	"/api/videos/").authenticated()
                // Channel component security
                .antMatchers(HttpMethod.DELETE,	"/api/channels/").authenticated()
                // Category component security
                .antMatchers(HttpMethod.POST,	"/api/categories").authenticated()
                .antMatchers(HttpMethod.PUT,	"/api/categories/").authenticated()
                .antMatchers(HttpMethod.DELETE,	"/api/categories/").authenticated()
                // Allow any other request
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
            	.logoutSuccessUrl("/")
                .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
    	final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        final UserDetails user =
             User.withUsername(defaultUsername)
                .password(encoder.encode(defaultPassword))
                .roles(USER_ROLE)
                .build();

        return new InMemoryUserDetailsManager(user);
    }
    
}
