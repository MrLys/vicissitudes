describe('Registration test', function () {
    it('Visit Rutta.no', function () {
        cy.visit('http://localhost:8080')
        cy.contains('Log in')
        cy.contains('About')
        cy.contains('Home')
        cy.contains('Register').click()
        cy.url().should('include', '/register')
        cy.get('#email')
            .type('fake@email.com')
            .should('have.value', 'fake@email.com')
        cy.get('#password1')
            .type('123')
            .should('have.value', '123')
        cy.contains('Register').click()
        cy.contains('The two passwords you entered are not equal')
        cy.get('#password2')
            .type('123')
            .should('have.value', '123')
        cy.contains('Register').click()
        cy.contains('The password must contain at least 8 characters')
        // clear
        cy.get('#password1').type('hi').clear()
        cy.get('#password2').type('hi').clear()

        cy.get('#password1')
            .type('password')
            .should('have.value', 'password')

        cy.get('#password2')
            .type('password')
            .should('have.value', 'password')
        cy.contains('Register').click()
        cy.contains('This is a top-10 common password')

        // clear
        cy.get('#password1').type('hi').clear()
        cy.get('#password2').type('hi').clear()
        cy.get('#email').type('hi').clear()

        var rand = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
        var email = rand + "@test.no";
        cy.get('#email')
            .type(email)
            .should('have.value', email)

        cy.get('#password1')
            .type(rand)
            .should('have.value', rand)

        cy.get('#password2')
            .type(rand)
            .should('have.value', rand)
        cy.contains('Register').click()

        cy.url().should('include', '/register')

        //successfull registration
        cy.contains("Congratulations, you've successfully created an account! 🎉")
        cy.contains("A link to activate your account has been emailed to " + email)
    })
})
