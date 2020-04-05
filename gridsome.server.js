// Server API makes it possible to hook into various parts of Gridsome
// on server-side and add custom data to the GraphQL data layer.
// Learn more: https://gridsome.org/docs/server-api/

// Changes here require a server restart.
// To restart press CTRL + C in terminal and run `gridsome develop`
const axios = require('axios')
var proxy = require("http-proxy-middleware");
module.exports = function (api) {
    api.chainWebpack(config => {
        config.mode('development')
    });
    api.configureServer(app => {
        app.use(
            '/api',
            proxy({
                target: process.env.RUTTA_URL || "http://localhost:2999/api",
            changeOrigin: true,
            pathRewrite: {
                '^/api': '/api'
            }
        })
        );
    });

  //api.loadSource(async actions => {
  //  const { data } = await axios.get('http://localhost:3000/api/choices');
  //
  //  const collection = actions.addCollection('Choices')
  //  const keys = Object.keys(data);
  //    console.log(keys);
  //    console.log(data);
  //  for(const key in keys) {

  //    collection.addNode({
  //      day: keys[key],
  //      choice: data[keys[key]]
  //    });
  //  }
  //});

  api.createPages(({ createPage }) => {
    // Use the Pages API here: https://gridsome.org/docs/pages-api/
  })
}
