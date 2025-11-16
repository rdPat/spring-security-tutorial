    package com.springsec.tutorial.config;

    import com.springsec.tutorial.service.MyUserDetailsService;
    import jakarta.servlet.Filter;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    import java.util.List;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig
    {
        private final MyUserDetailsService myUserDetailsService;
        private final PasswordEncoder passwordEncoder;

        @Autowired
        private JwtFilter jwtFilter;

        public SecurityConfig(MyUserDetailsService myUserDetailsService, PasswordEncoder passwordEncoder) {
            this.myUserDetailsService = myUserDetailsService;
            this.passwordEncoder = passwordEncoder;
        }


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


            http.csrf(c->c.disable())
                    .sessionManagement(s
                            ->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(a->a.requestMatchers("/create/product/**","/get/products/**","/get/user/{username}/**",
                                    "/fetch/users/**")
                            .authenticated()
                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/v3/api-docs.yaml"
                            ).permitAll()
                            .requestMatchers("/create/user/**","/user/present/{username}/{password}/**","/login/**").permitAll()

                    )

                    .formLogin(f->f.disable())
                    //using jwt authentication so disable it
                    .httpBasic(b->b.disable())
                    .authenticationProvider(authenticationProvider(myUserDetailsService,passwordEncoder))
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
            ;

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

        //this will expose authentication manager for bean creation without it authentication not work
        //authenticationConfiguration class automatically knows about
        //your UserDetailService, AuthenticationProvider,PasswordEncoder

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }


    }
