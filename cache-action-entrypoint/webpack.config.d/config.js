/*
 * Copyright 2020 Vladimir Sitnikov <sitnikov.vladimir@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

config.output = config.output || {}
config.output.globalObject = "this"
config.target = "node"

config.resolve.modules.unshift("src/test/resources")

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
                         mangle: false,
                         sourceMap: true,
                         // compress: false,
                         keep_classnames: /AbortSignal/,
                         keep_fnames: /AbortSignal/,
                         output: {
                           beautify: true,
                           indent_level: 1
                         }
                       }
                     }),
  ],
};
