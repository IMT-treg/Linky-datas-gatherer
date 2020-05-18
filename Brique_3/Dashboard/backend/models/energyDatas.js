const mongoose = require('mongoose'); 

const energyDatasSchema = mongoose.Schema({
    BASE : {type : String, required : true},
    ISOUSC : {type : String, required : true},
    PAPP : {type : String, required : true},
    ADCO : {type : String , required : true},
    timestamp : {type : String, required : true}
});

module.exports = mongoose.model('energyDatas',energyDatasSchema,'energy_info');