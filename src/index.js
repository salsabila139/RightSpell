// index.js
const Hapi = require('@hapi/hapi');
const Inert = require('@hapi/inert');
const Vision = require('@hapi/vision');
const HapiSwagger = require('hapi-swagger');
const Pack = require('../package.json');
const authRoutes = require('./routes/authRoutes');
const userRoutes = require('./routes/userRoutes');
const textRoutes = require('./routes/textRoutes');
const pdfRoutes = require('./routes/pdfRoutes');

const init = async () => {
    const server = Hapi.server({
        host: process.env.NODE_ENV !== 'production' ? 'localhost' : '0.0.0.0',
        port: process.env.PORT || 3000,
    });

    // Register plugins
    await server.register([
        Inert,
        Vision,
        {
            plugin: HapiSwagger,
            options: {
                info: {
                    title: 'API Documentation',
                    version: Pack.version,
                },
            },
        },
    ]);

    // Add routes
    server.route([...authRoutes, ...userRoutes, ...pdfRoutes, ...textRoutes]);

    server.route({
        method: 'GET',
        path: '/',
        handler: (request, h) => {
            return h.redirect('/documentation');
        }
    });

    server.route({
        method: '*',
        path: '/{any*}',
        handler: (request, h) => {
            return h.response({ statusCode: 404, error: "Not Found", message: "Page not found" }).code(404);
        }
    });

    await server.start();
    console.log('Server running on %s', server.info.uri);

    server.events.on('response', (request) => {
        console.log(`${request.info.remoteAddress}: ${request.method.toUpperCase()} ${request.path} --> ${request.response.statusCode}`);
    });
};

process.on('unhandledRejection', (err) => {
    console.log(err);
    process.exit(1);
});

init();
