//VARIABLES
//var used to display the gauge chart
var chart;
//var used to save the ajax query datas
var resultDatas;

//METHODS
//Ajax methods which get the server datas
$.ajax({
   url: "http://localhost:3000/api/dashboard/",
   success: display
});

//this function display the gauge and the graph when the datas are loaded
function display(result) {
   //deepcopy the result of the ajax query
   resultDatas = JSON.parse(JSON.stringify(result));
   //display the last one hour consumption graph
   displayHistoryOneHour();
   //display the realtime consumption gauge
   displayConsumption();
}

//Function to update the gauge every second
function requestData() {
   $.ajax({
       url: 'http://localhost:3000/api/dashboard/',
       success: function(result) {
         var lastData = result[result.length-1].value3;
         var suscribedEnergy = result[0].value1;
         var actualEnergyConsumed = [];
         //dividing by 5 because we convert A to VA. More details in the project report
         actualEnergyConsumed.push(Math.round(lastData/(suscribedEnergy*(1000/5))*100));
         chart.series[0].setData(actualEnergyConsumed);
         console.log(actualEnergyConsumed);
         setTimeout(requestData, 1000); 
       },
       cache: false
   });
}

//Display the gauge which calculate the actual consumption compared to the subsribed power in %
function displayConsumption() {
   chart = Highcharts.chart('gauge', {

      chart: {
         type: 'gauge',
         plotBackgroundColor: null,
         plotBackgroundImage: null,
         plotBorderWidth: 0,
         plotShadow: false,
         events: {
            load : requestData
         },
      },

      title: {
         text: 'Consommation actuelle'
      },

      pane: {
         startAngle: -150,
         endAngle: 150,
         background: [{
            backgroundColor: {
               linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
               stops: [
                  [0, '#FFF'],
                  [1, '#333']
               ]
            },
            borderWidth: 0,
            outerRadius: '109%'
         }, {
            backgroundColor: {
               linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
               stops: [
                  [0, '#333'],
                  [1, '#FFF']
               ]
            },
            borderWidth: 1,
            outerRadius: '107%'
         }, {
            // default background
         }, {
            backgroundColor: '#DDD',
            borderWidth: 0,
            outerRadius: '105%',
            innerRadius: '103%'
         }]
      },

      // the value axis
      yAxis: {
         min: 0,
         max: 100,

         minorTickInterval: 'auto',
         minorTickWidth: 1,
         minorTickLength: 10,
         minorTickPosition: 'inside',
         minorTickColor: '#666',

         tickPixelInterval: 30,
         tickWidth: 2,
         tickPosition: 'inside',
         tickLength: 10,
         tickColor: '#666',
         labels: {
            step: 2,
            rotation: 'auto'
         },
         title: {
            text: '%',
            style: {
               fontSize : '35px'
            }
         },
         plotBands: [{
            from: 0,
            to: 50,
            color: '#55BF3B' // green
         }, {
            from: 50,
            to: 80,
            color: '#DDDF0D' // yellow
         }, {
            from: 80,
            to: 100,
            color: '#DF5353' // red
         }]
      },

      series: [{
         name: 'Consommation',
         data: [],
         tooltip: {
            valueSuffix: '%'
         }
      }]

   });
}

//MANAGE BUTTON ACTIONS
//These buttons allow to change the view of the history graph
function buttonOne(){
   displayHistoryOneHour();
}
function buttonTwo(){
   displayHistoryEightHours()
}
function buttonThree(){
   displayHistoryTwentyFourHours()
}
function buttonFour(){
   displayHistoryOneWeek()
}

//find the datas of the last hour
function dataOneHour() {
   data = [];
   var oldValue;
   var actualDate = new Date();
   console.log(actualDate);
   oldDate = actualDate.setHours(actualDate.getHours() - 1);
   oldDate = new Date(oldDate);
   oldDateMinusOne = oldDate.setMinutes(oldDate.getMinutes() - 1);

   //loop to find the value 1 minute before the beggining of the hour
   for (var i = 0; i < resultDatas.length; i++) {
      var obj = resultDatas[i];
      date = parseInt(obj.timesStamp0);
      if (date > oldDateMinusOne) {
         oldValue = parseInt(obj.value2);
         break;
      }
   }
   for (var i = 0; i < resultDatas.length; i++) {
      var obj = resultDatas[i];
      date = parseInt(obj.timesStamp0);
      if (date > oldDate) {
         var vector = [];
         vector.push(date);
         vector.push((parseInt(obj.value2) - oldValue));
         data.push(vector);
         oldDate.setMinutes(oldDate.getMinutes() + 1);
         oldValue = parseInt(obj.value2);

      }
   }
   return data;
}
//Display the history graph with datas every minutes in the last hour
function displayHistoryOneHour() {
   data = dataOneHour();
   Highcharts.chart('history', {
      time: {
         useUTC: false
     },
      title: {
         text: 'Historique de consommation'
      },
      xAxis: {
         title: 'Temps',
         type: 'datetime'
      },
      yAxis: {
         title: {
            text: 'Consommation en Wh'
         },
         plotLines: [{
            value: 0,
            width: 1,
            color: '#808080'
         }]
      },
      series: [
         {  name : 'Consommation par minutes sur la dernière heure',
            data: data }
      ]
   });
}
//find the datas of the eight last hours
function dataEightHours(){
   data = [];
   var oldValue;
   var actualDate = new Date();
   console.log(actualDate);
   oldDate = actualDate.setHours(actualDate.getHours() - 8);
   oldDate = new Date(oldDate);
   oldDateMinusOne = oldDate.setHours(oldDate.getHours() - 1);

   //loop to find the value 1 hour before the beggining
   for (var i = 0; i < resultDatas.length; i++) {
      var obj = resultDatas[i];
      date = parseInt(obj.timesStamp0);
      if (date > oldDateMinusOne) {
         oldValue = parseInt(obj.value2);
         break;
      }
   }
   for (var i = 0; i < resultDatas.length; i++) {
      var obj = resultDatas[i];
      date = parseInt(obj.timesStamp0);
      if (date > oldDate) {
         var vector = [];
         vector.push(date);
         vector.push((parseInt(obj.value2) - oldValue));
         data.push(vector);
         oldDate.setHours(oldDate.getHours() + 1);
         oldValue = parseInt(obj.value2);

      }
   }
   return data;
}
//Display the history graph with datas every hours in the last 8 hours
function displayHistoryEightHours() {
   data = dataEightHours();
   Highcharts.chart('history', {
      time: {
         useUTC: false
     },
      title: {
         text: 'Historique de consommation'
      },
      xAxis: {
         title: 'Temps',
         type: 'datetime'
      },
      yAxis: {
         title: {
            text: 'Consommation en Wh'
         },
         plotLines: [{
            value: 0,
            width: 1,
            color: '#808080'
         }]
      },
      series: [
         {  name : 'Consommation par heure sur les 8 dernières heure',
            data: data }
      ]
   });
}
//find the datas of the twenty-four last hours
function dataTwentyFourHours(){
   data = [];
   var oldValue;
   var actualDate = new Date();
   console.log(actualDate);
   oldDate = actualDate.setHours(actualDate.getHours() - 24);
   oldDate = new Date(oldDate);
   oldDateMinusOne = oldDate.setHours(oldDate.getHours() - 1);

   //loop to find the value 1 hour before the beggining
   for (var i = 0; i < resultDatas.length; i++) {
      var obj = resultDatas[i];
      date = parseInt(obj.timesStamp0);
      if (date > oldDateMinusOne) {
         oldValue = parseInt(obj.value2);
         break;
      }
   }
   for (var i = 0; i < resultDatas.length; i++) {
      var obj = resultDatas[i];
      date = parseInt(obj.timesStamp0);
      if (date > oldDate) {
         var vector = [];
         vector.push(date);
         vector.push((parseInt(obj.value2) - oldValue));
         data.push(vector);
         oldDate.setHours(oldDate.getHours() + 1);
         oldValue = parseInt(obj.value2);

      }
   }
   return data;
}
//Display the history graph with datas every hours in the last 24 hours
function displayHistoryTwentyFourHours() {
   data = dataTwentyFourHours();
   Highcharts.chart('history', {
      time: {
         useUTC: false
     },
      title: {
         text: 'Historique de consommation'
      },
      xAxis: {
         title: 'Temps',
         type: 'datetime'
      },
      yAxis: {
         title: {
            text: 'Consommation en Wh'
         },
         plotLines: [{
            value: 0,
            width: 1,
            color: '#808080'
         }]
      },
      series: [
         {  name : 'Consommation par heure sur les 24 dernières heures',
            data: data }
      ]
   });
}
//find the datas of the last week
function dataOneWeek(){
   data = [];
   var oldValue;
   var actualDate = new Date();
   console.log(actualDate);
   oldDate = actualDate.setHours(actualDate.getHours() - (7*24));
   oldDate = new Date(oldDate);
   oldDateMinusOne = oldDate.setHours(oldDate.getHours() - 24);

   //loop to find the value 1 day before the beggining
   for (var i = 0; i < resultDatas.length; i++) {
      var obj = resultDatas[i];
      date = parseInt(obj.timesStamp0);
      if (date > oldDateMinusOne) {
         oldValue = parseInt(obj.value2);
         break;
      }
   }
   for (var i = 0; i < resultDatas.length; i++) {
      var obj = resultDatas[i];
      date = parseInt(obj.timesStamp0);
      if (date > oldDate) {
         var vector = [];
         vector.push(date);
         vector.push((parseInt(obj.value2) - oldValue));
         data.push(vector);
         oldDate.setHours(oldDate.getHours() + 24);
         oldValue = parseInt(obj.value2);

      }
   }
   return data;
}

//Display the history graph with datas every day in the last week
function displayHistoryOneWeek() {
   data = dataOneWeek();
   Highcharts.chart('history', {
      time: {
         useUTC: false
     },
      title: {
         text: 'Historique de consommation'
      },
      xAxis: {
         title: 'Temps',
         type: 'datetime'
      },
      yAxis: {
         title: {
            text: 'Consommation en Wh'
         },
         plotLines: [{
            value: 0,
            width: 1,
            color: '#808080'
         }]
      },
      series: [
         {  name : 'Consommation par jours sur la semaine passée',
            data: data }
      ]
   });
}
