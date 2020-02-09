function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}
describe('My First Test', function () {
    it('Try forgot password at Rutta.no', function () {
        cy.visit('http://localhost:8080')
        cy.contains('Log in')
        cy.contains('About')
        cy.contains('Home')
        cy.contains('Register')
        cy.contains('Log in').click()

        cy.server()
        cy.route('POST', '/api/habit').as('new-habit')

        cy.url().should('include', '/login')
        cy.get("#forgot-password").click()
        cy.url().should('include', '/forgot-password')
        cy.contains('Lost password')
        var rand = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
        var email = rand + "@test.no";
        var login = 'e8ms2cu1fese7h8k6fcog@test.no'
        cy.get('#email')
            .type(email)
            .should('have.value', email)
            
        cy.contains('Send recovery link!').click()
        cy.url().should('include', '/forgot-password')
        cy.contains('Invalid email!')
        // Officially logged in. Create a habit
        // Create a new habit with random values
        // clear
        cy.get('#email').type('hi').clear()
        cy.get('#email')
            .type(login)
            .should('have.value', login)

        cy.contains('Send recovery link!').click()
        cy.url().should('include', '/forgot-password')
        cy.contains('The receovery link is on it\'s way!')
        cy.contains('A link to recovery your account has been emailed to e8ms2cu1fese7h8k6fcog@test.no')
    })
})
