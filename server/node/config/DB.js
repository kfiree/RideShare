const { Client } = require('pg');
const URL = "postgres://jrtfpxkjouseuk:ee06e29a206ed56595fb998c7ab814f2b0ebce8a46a6cc088d614255c64c7ec4@ec2-34-246-227-219.eu-west-1.compute.amazonaws.com:5432/de3tg1mpkboq65";

const client = new Client({
    connectionString: URL,
    ssl: {
        rejectUnauthorized: false
    }
});

const connect = () => {
    client.connect();
    console.log('database connected...');
}
connect();

module.exports = client;
