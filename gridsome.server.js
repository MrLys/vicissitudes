// Server API makes it possible to hook into various parts of Gridsome
// on server-side and add custom data to the GraphQL data layer.
// Learn more: https://gridsome.org/docs/server-api/

// Changes here require a server restart.
// To restart press CTRL + C in terminal and run `gridsome develop`
const axios = require('axios')
var proxy = require("http-proxy-middleware");
module.exports = function (api) {
    api.configureServer(app => {
        app.use(
            '/api',
            proxy({
            target: 'http://localhost:3000',
            changeOrigin: true,
            pathRewrite: {
                '^/api': '/api'
            }
        })
        );
    });

  api.createPages(({ createPage }) => {
    // Use the Pages API here: https://gridsome.org/docs/pages-api/
  })
}
