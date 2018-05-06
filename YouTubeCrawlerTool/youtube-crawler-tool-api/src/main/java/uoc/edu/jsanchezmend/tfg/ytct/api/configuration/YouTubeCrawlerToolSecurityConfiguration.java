package uoc.edu.jsanchezmend.tfg.ytct.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class YouTubeCrawlerToolSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
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
        UserDetails user =
             User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
    
}