'use strict';

function _interopDefault (ex) { return (ex && (typeof ex === 'object') && 'default' in ex) ? ex['default'] : ex; }

var utilsLite = require('utils-lite');
require('echarts-wordcloud');
var Core = _interopDefault(require('./core'));

function getSeries(args) {
  var dimension = args.dimension,
      metrics = args.metrics,
      rows = args.rows,
      color = args.color,
      sizeMax = args.sizeMax,
      sizeMin = args.sizeMin,
      shape = args.shape;


  var baseType = {
    type: 'wordCloud',
    textStyle: {
      normal: {
        color: !utilsLite.isArray(color) && !!color ? color : function () {
          return 'rgb(' + [Math.round(Math.random() * 160), Math.round(Math.random() * 160), Math.round(Math.random() * 160)].join(',') + ')';
        }
      }
    },
    shape: shape,
    sizeRange: [sizeMin, sizeMax]
  };

  var len = utilsLite.isArray(color) ? color.length : 0;
  var data = rows.slice().map(function (row) {
    var text = {
      name: row[dimension],
      value: row[metrics]
    };

    if (len > 0) {
      text.textStyle = {
        normal: {
          color: color[Math.floor(Math.random() * len)]
        }
      };
    }
    return text;
  });

  baseType.data = data;

  return [baseType];
}

function getTooltip(args) {
  var tooltipFormatter = args.tooltipFormatter;


  return {
    show: true,
    formatter: function formatter(params) {
      var _params$data = params.data,
          name = _params$data.name,
          value = _params$data.value;


      if (tooltipFormatter) {
        return tooltipFormatter.apply(null, params);
      }

      return name + ': ' + value;
    }
  };
}

var wordcloud = function wordcloud(columns, rows, settings, extra) {
  var _settings$dimension = settings.dimension,
      dimension = _settings$dimension === undefined ? columns[0] : _settings$dimension,
      _settings$metrics = settings.metrics,
      metrics = _settings$metrics === undefined ? columns[1] : _settings$metrics,
      _settings$color = settings.color,
      color = _settings$color === undefined ? '' : _settings$color,
      _settings$sizeMax = settings.sizeMax,
      sizeMax = _settings$sizeMax === undefined ? 60 : _settings$sizeMax,
      _settings$sizeMin = settings.sizeMin,
      sizeMin = _settings$sizeMin === undefined ? 12 : _settings$sizeMin,
      _settings$shape = settings.shape,
      shape = _settings$shape === undefined ? 'circle' : _settings$shape;
  var tooltipVisible = extra.tooltipVisible,
      tooltipFormatter = extra.tooltipFormatter;


  var series = getSeries({ dimension: dimension, metrics: metrics, rows: rows, color: color, sizeMax: sizeMax, sizeMin: sizeMin, shape: shape });
  var tooltip = tooltipVisible && getTooltip({ tooltipFormatter: tooltipFormatter });

  return {
    series: series,
    tooltip: tooltip
  };
};

var _extends = Object.assign || function (target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i];

    for (var key in source) {
      if (Object.prototype.hasOwnProperty.call(source, key)) {
        target[key] = source[key];
      }
    }
  }

  return target;
};

var index = _extends({}, Core, {
  name: 'VeWordcloud',
  data: function data() {
    this.chartHandler = wordcloud;
    return {};
  }
});

module.exports = index;
