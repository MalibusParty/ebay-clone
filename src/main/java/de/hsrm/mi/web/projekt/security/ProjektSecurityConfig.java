//package de.hsrm.mi.web.projekt.security;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class ProjektSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    ProjektUserDetailService projektUserDetailService;
//
//    @Bean PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        PasswordEncoder pwenc = passwordEncoder();
//
//        authenticationManagerBuilder
//                .userDetailsService(projektUserDetailService)
//                .passwordEncoder(passwordEncoder());
//
//        authenticationManagerBuilder.inMemoryAuthentication()
//                .withUser("friedfert")
//                .password(pwenc.encode("dingdong"))
//                .roles("USER")
//        .and()
//                .withUser("joghurta")
//                .password(pwenc.encode("chefin"))
//                .roles("ADMIN");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/css/**").permitAll()
//                .antMatchers("/registrieren", "/logout").permitAll()
//                .antMatchers("/h2-console/**").permitAll()
//                .antMatchers("/api/**").permitAll()
//                .antMatchers("/topic/**", "/stopmbroker").permitAll()
//                .anyRequest().authenticated()
//            .and()
//                .formLogin()
//                .defaultSuccessUrl("/benutzerprofil")
//                .permitAll()
//            .and()
//                .logout()
//                .logoutSuccessUrl("/login")
//                .permitAll();
//
//        http.csrf().ignoringAntMatchers("/h2-console/**");
//
//        http.headers().frameOptions().disable();
//    }
//
//}
