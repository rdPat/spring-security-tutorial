    package com.springsec.tutorial.config;

    import com.springsec.tutorial.service.MyUserDetailsService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.NoOpPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.provisioning.InMemoryUserDetailsManager;
    import org.springframework.security.web.SecurityFilterChain;

    import java.util.List;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig
    {
        private final MyUserDetailsService myUserDetailsService;
        private final PasswordEncoder passwordEncoder;

        public SecurityConfig(MyUserDetailsService myUserDetailsService, PasswordEncoder passwordEncoder) {
            this.myUserDetailsService = myUserDetailsService;
            this.passwordEncoder = passwordEncoder;
        }


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.csrf(c->c.disable())
                    .sessionManagement(s
                            ->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(a->a.requestMatchers("/create/product/**","/get/products/**")
                            .authenticated()
                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/v3/api-docs.yaml"
                            ).permitAll()
                            .requestMatchers("/create/user/**","/fetch/users/**").permitAll()
                    )
                    .formLogin(f->f.disable())
                    .httpBasic(Customizer.withDefaults());

            return http.build();
        }

        //creating users
    //    @Bean
    //    public UserDetailsService userDetailsService()
    //    {
    //        UserDetails user1= User.withDefaultPasswordEncoder()
    //                .username("ram")
    //                .password("1")
    //                .roles("ADMIN")
    //                .build();
    //        UserDetails user2= User.withDefaultPasswordEncoder()
    //                .username("user")
    //                .password("1")
    //                .roles("USER")
    //                .build();
    //        return new InMemoryUserDetailsManager(List.of(user1,user2));
    //    }



        @Bean
        public AuthenticationProvider authenticationProvider(MyUserDetailsService myUserDetailsService,PasswordEncoder passwordEncoder)
        {
            DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
            provider.setPasswordEncoder(passwordEncoder);
            //provider.setPasswordEncoder(new BCryptPasswordEncoder());   NOT NEEDED
            provider.setUserDetailsService(myUserDetailsService);

            return provider;
        }


    }
