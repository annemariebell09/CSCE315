const weather = require('weather-js');

const prompt = require('prompt-sync')();

var location = prompt("Enter your Location: ");

weather.find(
  { search: location, degreeType: 'F' },
  (error, result) => {
    if (error) console.error(error);

    console.log(JSON.stringify(result, null, 2));
  },
);