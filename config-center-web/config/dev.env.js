let merge = require('webpack-merge');
let prodEnv = require('./prod.env');

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  WS_ADDRES:'"ws://10.11.155.110:18080"'
    //WS_ADDRES:'"ws://localhost:18080"'
});
