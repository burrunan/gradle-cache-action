config.output = config.output || {}
config.output.globalObject = "this"
config.target = "node"

const TerserPlugin = require('terser-webpack-plugin');

// keep_classnames is required to workaround node-fetch Expected signal to be an instanceof AbortSignal
config.optimization = {
  minimizer: [
    new TerserPlugin({
                       cache: true,
                       parallel: true,
                       sourceMap: true, // Must be set to true if using source-maps in production
                       terserOptions: {
                         // https://github.com/webpack-contrib/terser-webpack-plugin#terseroptions
                         // mangle: false,
                         sourceMap: true,
                         // compress: false,
                         keep_classnames: /AbortSignal/,
                         keep_fnames: /AbortSignal/,
                       }
                     }),
  ],
};
