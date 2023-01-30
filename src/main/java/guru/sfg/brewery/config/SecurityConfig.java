package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncodersFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

/*    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public RestUrlAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager) {
        RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }*/

    @Bean
    PasswordEncoder passwordEncoder() {
        return SfgPasswordEncodersFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
/*        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

        http.addFilterBefore(restUrlAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);*/

        http.csrf().disable();

        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll() // do not use in production !
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                            //.antMatchers(HttpMethod.GET, "/api/v1/beer/**").hasAnyRole("ADMIN","CUSTOMER","USER")
                            //.mvcMatchers(HttpMethod.DELETE,"/api/v1/beer/**").hasRole("ADMIN")
                            //.mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").hasAnyRole("ADMIN","CUSTOMER","USER")
                            .mvcMatchers("/brewery/breweries").hasAnyRole("ADMIN","CUSTOMER")
                            .mvcMatchers(HttpMethod.GET,"/brewery/api/v1/breweries").hasAnyRole("ADMIN","CUSTOMER")
                            .mvcMatchers("/beers/find", "/beers/{beerID}").hasAnyRole("ADMIN","CUSTOMER","USER");
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();

        // for h2-database to work with spring security
        http.headers().frameOptions().sameOrigin();
    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("guru")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(admin, user);
//    }

//    @Autowired
//    JpaUserDetailsService jpaUserDetailsService;

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(this.jpaUserDetailsService).passwordEncoder(passwordEncoder());

//        auth.inMemoryAuthentication()
//                .withUser("spring")
//                .password("{bcrypt}$2a$10$UPwkptmTCOMnWNm6H/n.muJ4jzl7AnDVz67fbshdJY8mTFB/32R5O")
//                .roles("ADMIN")
//                .and()
//                .withUser("user")
//                .password("{sha256}e5cfcda30caf0ea2dc6ee3854fc98c1e386a0c4cee55fb5539b5e6e0c5a6a9cf0dca37018e67d2ce")
//                .roles("USER")
//                .and()
//                .withUser("scoott")
//                .password("{bcrypt15}$2a$15$d6px.rFY7RRQdDVOYJtlrumjLGDhyB0qhufSrVHyMuhUdeKndR312")
//                .roles("CUSTOMER");
//    }
}
