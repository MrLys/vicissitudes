describe('My First Test', function () {
    it('Log into Rutta.no and create some habits to track', function () {
        cy.visit('http://localhost:8080')
        cy.contains('Log in')
        cy.contains('About')
        cy.contains('Home')
        cy.contains('Register')
        cy.contains('Log in').click()

        cy.server()
        cy.route('POST', '/api/habit').as('new-habit')

        cy.url().should('include', '/login')
        var login = 'e8ms2cu1fese7h8k6fcog@test.no'
        var pwd = 'e8ms2cu1fese7h8k6fcog'
        cy.get('#email')
            .type(login)
            .should('have.value', login)

        cy.get('#password')
            .type(pwd)
            .should('have.value', pwd)
        cy.contains('Log in').click()
        cy.url().should('include', '/habits')
        // Officially logged in. Create a habit
        // Create a new habit with random values
        cy.get('#add-habit').click()
        var rand = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
        cy.get('#habit-field')
            .type(rand+'{enter}')
        cy.wait('@new-habit').should('have.property', 'status', 200)
        cy.contains(rand)

        // wait for the page to refresh
        // try to add the same habit again
        cy.get('#add-habit').click()
        cy.get('#habit-field')
            .type(rand)
        cy.contains(rand)
        cy.get('#save-habit').click()

        // wait for the page to refresh
        cy.wait('@new-habit').should('have.property', 'status', 409)

        // add Exercising
        cy.get('#add-habit').click()
        // Not cleared when already exists.
        cy.get('#habit-field').type('hi').clear()

        cy.get('#habit-field').type('Exercising{enter}')
        // wait for the page to refresh
        cy.wait('@new-habit').should('have.property', 'status', 200)

        cy.contains('Exercising')

        // Mark monday groove ass successfull
        cy.get('#Exercising-Monday').click()
        cy.get('#success-button').click()
        cy.get('#Exercising-Monday-groove')
        .should('have.class', 'bg-ocean_green-light')

        cy.get('#Exercising-Tuesday').click()
        cy.get('#fail-button').click()
        cy.get('#Exercising-Tuesday-groove')
        .should('have.class', 'bg-tango_pink-light')

        cy.get('#Exercising-Friday').click()
        cy.get('#pass-button').click()

        cy.get('#Exercising-Friday-groove')
        .should('have.class', 'stripes')

        cy.visit('http://localhost:8080/habits')
        // make sure they persist on refresh
        cy.get('#Exercising-Monday-groove')
        .should('have.class', 'bg-ocean_green-light')
        cy.get('#Exercising-Tuesday-groove')
        .should('have.class', 'bg-tango_pink-light')
        cy.get('#Exercising-Friday-groove')
        .should('have.class', 'stripes')

    })
})
