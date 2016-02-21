// Helper: root(), and rootDir() are defined at the bottom
var path = require('path');
var webpack = require('webpack');
var args = process.argv.slice(2);

// Webpack Plugins
//var CommonsChunkPlugin = webpack.optimize.CommonsChunkPlugin;
var autoprefixer = require('autoprefixer');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var ProvidePlugin = require('webpack/lib/ProvidePlugin');

module.exports = (function makeWebpackConfig() {
    /**
     * Environment type
     * BUILD is for generating minified builds
     */
    var BUILD = args.indexOf('--webpack-build') !== -1 || process.env['webpack-build'];
    var TEST = args.indexOf('--webpack-test') !== -1 || process.env['webpack-test'];

    /**
     * Config
     * Reference: http://webpack.github.io/docs/configuration.html
     * This is the object where all configuration gets set
     */
    var config = {};

    config.devtool = 'source-map';
    config.entry = {
        'vendor': './src/main/websrc/vendor.js',
        'app': './src/main/websrc/bootstrap.js' // our angular app
    };

    config.output = {
        path: root('webapp/static'),
        publicPath: '/lima/',
        filename: 'js/[name].js',
        chunkFilename: BUILD ? '[id].chunk.js?[hash]' : '[id].chunk.js'
    };
    config.resolve = {
        cache: true,
        root: root(),
        // only discover files that have those extensions
        extensions: ['', '.js', '.json', '.css', '.scss', '.html'],
        alias: {
            'app': 'websrc/app',
            'common': 'websrc/common'
        }
    };

    config.module = {
        loaders: [
            {test: /\.(png|jpe?g|gif|svg|woff|woff2|ttf|eot|ico)$/, loader: 'file?name=[path][name].[ext]?[hash]'},

            {test: /\.json$/, loader: 'json'},

            {
                test: /\.css$/,
                exclude: root('websrc', 'app'),
                loader: TEST ? 'null' : ExtractTextPlugin.extract('style', 'css?sourceMap!postcss')
            },
            // all css required in src/app files will be merged in js files
            {test: /\.css$/, include: root('websrc', 'app'), loader: 'raw!postcss'},

            {
                test: /\.scss$/,
                exclude: root('websrc', 'app'),
                loader: TEST ? 'null' : ExtractTextPlugin.extract('style', 'css?sourceMap!postcss!sass')
            },
            // all css required in src/app files will be merged in js files
            {test: /\.scss$/, exclude: root('websrc', 'style'), loader: 'raw!postcss!sass'},

            {test: /\.html$/, loader: 'raw'}
        ],
        postLoaders: [],
        noParse: [/.+zone\.js\/dist\/.+/]
    };

    config.plugins = [];
    config.debug = true;

    config.externals = {
        // require("jquery") is external and available
        //  on the global var jQuery
        "jquery": "jQuery",
        "angular": "angular",
        "restangular": "angular"
    };

    config.plugins.push(
        new webpack.optimize.CommonsChunkPlugin({
            name: 'vendor',
            filename: 'js/[name].js',
            minChunks: Infinity
        }),
        new webpack.optimize.CommonsChunkPlugin({
            name: 'common',
            filename: 'js/[name].js',
            minChunks: 2,
            chunks: ['app', 'vendor']
        }),

        new HtmlWebpackPlugin({
            template: './src/main/websrc/public/index.html',
            inject: 'body',
            hash: true, // inject ?hash at the end of the files
            chunksSortMode: function compare(a, b) {
                // common always first
                if (a.names[0] === 'common') {
                    return -1;
                }
                // app always last
                if (a.names[0] === 'app') {
                    return 1;
                }
                // vendor before app
                if (a.names[0] === 'vendor' && b.names[0] === 'app') {
                    return -1;
                } else {
                    return 1;
                }
                // a must be equal to b
                return 0;
            }
        }),
        new ExtractTextPlugin('css/[name].css', {disable: false})

        // jQuery
        //new ProvidePlugin({
        //    jQuery: 'jquery',
        //    $: 'jquery',
        //    jquery: 'jquery'
        //})
    );


    config.plugins.push(
        new webpack.NoErrorsPlugin(),
        new webpack.optimize.DedupePlugin(),
        new webpack.optimize.OccurenceOrderPlugin(),

        // Reference: http://webpack.github.io/docs/list-of-plugins.html#uglifyjsplugin
        // Minify all javascript, switch loaders to minimizing mode
        //new webpack.optimize.UglifyJsPlugin(),
        new CopyWebpackPlugin([{
            from: root('websrc/public')
        }])
    );

    config.postcss = [
        autoprefixer({
            browsers: ['last 2 version']
        })
    ];

    config.sassLoader = {
        //includePaths: [path.resolve(__dirname, "node_modules/foundation-sites/scss")]
    };
    config.devServer = {
        historyApiFallback: true,
        contentBase: './src/main/websrc/public'
    };

    return config;
})();

// Helper functions
function root(args) {
    var base = path.join(__dirname, "src/main");
    args = Array.prototype.slice.call(arguments, 0);
    return path.join.apply(path, [base].concat(args));
}

function rootNode(args) {
    args = Array.prototype.slice.call(arguments, 0);
    return root.apply(path, ['node_modules'].concat(args));
}
