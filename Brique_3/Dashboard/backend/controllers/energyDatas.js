const Data = require('../models/energyDatas');

exports.getAllDatas = function(req, res, next){
    Data.find()
        .then(things => res.status(200).json(things))
        .catch(error => res.status(404).json({error}));
}; 