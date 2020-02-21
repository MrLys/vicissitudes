describe('My First Test', function () {
    it('Try to activate account with invalid token', function () {
        cy.visit('http://localhost:8080/activation?token=1bbb2604-2825-4ba2-9730-f886e204bad1-22222') //invalid
        cy.contains('Something went wrong')
    })
})
