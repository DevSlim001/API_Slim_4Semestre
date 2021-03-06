package com.slim.manual.config;

import com.slim.manual.security.jwt.JwtAuthFilter;
import com.slim.manual.security.jwt.JwtService;
import com.slim.manual.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    /**
     * Criptografa senhas
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    /**
     * Configura a autenticação do usuário
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(usuarioService)
            .passwordEncoder(passwordEncoder());
    }

    /**
     * Configura a autorização
     */ 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and().csrf().disable()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/usuarios/auth")
                    .permitAll()
                .antMatchers(HttpMethod.POST,"/usuarios/valid")
                    .permitAll()
                .antMatchers(HttpMethod.PATCH,"/usuarios/senha/{email}")
                    .permitAll()
                .antMatchers(HttpMethod.POST,"/usuarios")
                    .hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/usuarios/**")
                    .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET,"/usuarios/**")
                    .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE,"/usuarios/**")
                    .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST,"/manual/**")
                    .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET,"/manual/**")
                    .hasAnyRole("USER", "ADMIN")
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Ignora as rotas
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
        );
    }
}
