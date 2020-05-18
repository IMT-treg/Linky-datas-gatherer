const express = require('express');

const router = express.Router();

const energyDatasCtrl = require('../controllers/energyDatas')

router.get('/', energyDatasCtrl.getAllDatas);

module.exports = router;