package com.mct;

import com.mct.domain.enums.Role;
import com.mct.domain.model.User;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

/**
 * Carrega dados iniciais no banco de dados para facilitar o treinamento.
 */
@ApplicationScoped
public class Startup {

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        if (User.count() == 0) {
            User admin = new User();
            admin.username = "admin";
            admin.passwordHash = "admin123"; // Simplificado
            admin.role = Role.ADMIN;
            admin.persist();

            User user = new User();
            user.username = "user";
            user.passwordHash = "user123"; // Simplificado
            user.role = Role.USER;
            user.persist();
        }
    }
}
