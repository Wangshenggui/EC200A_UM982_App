

window.onload = function() {
    // 页面加载完毕后执行
    console.log("页面加载完毕！");
};


// 获取所有的 NMEA 容器元素
const containers = document.querySelectorAll([
    '.nmea-message-dtm',
    '.nmea-message-gbs',
    '.nmea-message-gga',
    '.nmea-message-ggah',
    '.nmea-message-gll',
    '.nmea-message-gllh',
    '.nmea-message-gns',
    '.nmea-message-gnsh',
    '.nmea-message-gst',
    '.nmea-message-gsth',
    '.nmea-message-ths',
    '.nmea-message-rmc',
    '.nmea-message-rmch',
    '.nmea-message-rot',
    '.nmea-message-vtg',
    '.nmea-message-vtgh',
    '.nmea-message-zda'
  ].join(', '));
function scrollToContainer (index) {
    // 获取当前要滚动的容器
    const currentContainer = containers[index];
    // 滚动到当前容器
    if (currentContainer) {
        currentContainer.scrollIntoView({ behavior: "smooth" });
    }
}

function dmsToDecimal(dms) {
    // 提取度数部分
    let degrees = Math.floor(dms / 100);
    // 计算分钟部分
    let minutes = dms - (degrees * 100);
    // 转换为十进制度数
    let decimalDegrees = degrees + (minutes / 60.0);
    return decimalDegrees;
}

// 根据RTK状态值返回对应的文本
function getRTKStateText(rtkState) {
    switch (rtkState) {
        case 0:
            return '无定位';
        case 1:
            return '单点定位';
        case 2:
            return '亚米级定位';
        case 4:
            return 'RTK固定解';
        case 5:
            return 'RTK浮动解';
        default:
            return '未知';
    }
}


function isDTMData(Sentence) {
    return Sentence.startsWith('$GNDTM,') || Sentence.startsWith('$GPDTM,');
}
function parseDTM(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');

        var DatumCode = Parts[1];//本地坐标系代码
        var LatOffset = Parts[3];//纬度偏移量，单位分
        var LatDir = Parts[4];//纬度偏移标记
        var LonOffset = Parts[5];//经度偏移量，单位分
        var LonDir = Parts[6];//经度偏移标记
        var AltOffset = Parts[7];//海拔偏移量，单位米
        var RfDatumCode = Parts[8].slice(0, -3);//参考坐标系代码

        return {
            DatumCode: DatumCode,
            LatOffset: LatOffset,
            LatDir: LatDir,
            LonOffset: LonOffset,
            LonDir: LonDir,
            AltOffset: AltOffset,
            RfDatumCode: RfDatumCode,
        };
    } catch (e) {
        console.error("Failed to parse:", e);
        return null;
    }
}

function isGBSData(Sentence) {
    return Sentence.startsWith('$GNGBS,') || Sentence.startsWith('$GPGBS,');
}
function parseGBS(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');

        var UTC = Parts[1];
        var LatExp = Parts[2];
        var LonExp = Parts[3];
        var AltExp = Parts[4];
        var FaultySatelliteID = Parts[5];

        return {
            UTC: UTC,
            LatExp: LatExp,
            LonExp: LonExp,
            AltExp: AltExp,
            FaultySatelliteID: FaultySatelliteID,
        };
    } catch (e) {
        console.error("Failed to parse:", e);
        return null;
    }
}

function isGGAData(Sentence) {
    return Sentence.startsWith('$GNGGA,') || Sentence.startsWith('$GPGGA,');
}
function isGGAHData(Sentence) {
    return Sentence.startsWith('$GNGGAH,') || Sentence.startsWith('$GPGGAH,');
}
function parseGGA_H(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');
        
        UTC = Parts[1];
        Lat = Parts[2];
        LatDir = Parts[3];
        Lon = Parts[4];
        LonDir = Parts[5];
        Qual = Parts[6];
        NumberOfSatellites = Parts[7];
        HDOP = Parts[8];
        Alt = Parts[9];
        Undulation = Parts[11];
        Age = Parts[13];
        ID = Parts[14].slice(0,-3);


        return {
            UTC: UTC,
            Lat: Lat,
            LatDir: LatDir,
            Lon: Lon,
            LonDir: LonDir,
            Qual: Qual,
            NumberOfSatellites: NumberOfSatellites,
            HDOP: HDOP,
            Alt: Alt,
            Undulation: Undulation,
            Age: Age,
            ID: ID,
        };
    } catch (e) {
        console.error("Failed to parse:", e);
        return null;
    }
}

function isGLLData(Sentence) {
    return Sentence.startsWith('$GNGLL,') || Sentence.startsWith('$GPGLL,');
}
function isGLLHData(Sentence) {
    return Sentence.startsWith('$GNGLLH,') || Sentence.startsWith('$GPGLLH,');
}
function parseGLL_H(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');
        
        Lat = Parts[1];
        LatDir = Parts[2];
        Lon = Parts[3];
        LonDir = Parts[4];
        UTC = Parts[5];
        Status = Parts[6];
        PositioningSystemMode = Parts[7][0];


        return {
            Lat: Lat,
            LatDir: LatDir,
            Lon: Lon,
            LonDir: LonDir,
            UTC: UTC,
            Status: Status,
            PositioningSystemMode: PositioningSystemMode,
        };
    } catch (e) {
        console.error("Failed to parse:", e);
        return null;
    }
}

function isGNSData(Sentence) {
    return Sentence.startsWith('$GNGNS,') || Sentence.startsWith('$GPGNS,');
}
function isGNSHData(Sentence) {
    return Sentence.startsWith('$GNGNSH,') || Sentence.startsWith('$GPGNSH,');
}
function parseGNS_H(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');
        
        UTC = Parts[1];
		Lat = Parts[2];
        LatDir = Parts[3];
        Lon = Parts[4];
        LonDir = Parts[5];
        PositioningSystemMode = Parts[6];
        NumberOfSatellites = Parts[7];
        HDOP = Parts[8];
        AntAlt = Parts[9];
        GeoSep = Parts[10];
        Age = Parts[11];
        ID = Parts[12];
        Status = Parts[13][0];


        return {
            UTC: UTC,
			Lat: Lat,
            LatDir: LatDir,
            Lon: Lon,
            LonDir: LonDir,
            PositioningSystemMode: PositioningSystemMode,
            NumberOfSatellites: NumberOfSatellites,
            HDOP: HDOP,
            AntAlt: AntAlt,
            GeoSep: GeoSep,
            Age: Age,
            ID: ID,
            Status: Status
        };
    } catch (e) {
        console.error("Failed to parse:", e);
        return null;
    }
}

function isGSTData(Sentence) {
    return Sentence.startsWith('$GNGST,') || Sentence.startsWith('$GPGST,');
}
function isGSTHData(Sentence) {
    return Sentence.startsWith('$GNGSTH,') || Sentence.startsWith('$GPGSTH,');
}
function parseGST_H(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');
        
        UTC = Parts[1];
        RMS = Parts[2];
        SmjrStd = Parts[3];
        SmnrStd = Parts[4];
        Orient = Parts[5];
        LatStd = Parts[6];
        LonStd = Parts[7];
        AltStd = Parts[8].slice(0, -3);

        return {
            UTC: UTC,
            RMS: RMS,
            SmjrStd: SmjrStd,
            SmnrStd: SmnrStd,
            Orient: Orient,
            LatStd: LatStd,
            LonStd: LonStd,
            AltStd: AltStd
        };
    } catch (e) {
        console.error("Failed to parse:", e);
        return null;
    }
}

function isTHSData(Sentence) {
    return Sentence.startsWith('$GNTHS,') || Sentence.startsWith('$GPTHS,');
}
function parseTHS(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');

        Heading = Parts[1];
        Mode = Parts[2][0];

        return {
            Heading: Heading,
            Mode: Mode
        };
    } catch (e) {
        console.error("Failed to parse:", e);
        return null;
    }
}

function isRMCData(Sentence) {
    return Sentence.startsWith('$GNRMC,') || Sentence.startsWith('$GPRMC,');
}
function isRMCHData(Sentence) {
    return Sentence.startsWith('$GNRMCH,') || Sentence.startsWith('$GPRMCH,');
}
function parseRMC_H(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');

        UTC = Parts[1];                // UTC 时间
        Status = Parts[2];             // 定位状态 (A = 有效, V = 无效)
        Latitude = Parts[3];           // 纬度
        LatitudeDirection = Parts[4];  // 纬度方向 (N/S)
        Longitude = Parts[5];          // 经度
        LongitudeDirection = Parts[6]; // 经度方向 (E/W)
        Speed = Parts[7];              // 地面速率 (单位: knots)
        CourseOverGround = Parts[8];   // 地面航向 (COG)
        _Date = Parts[9];               // 日期 (ddmmyy 格式)
        MagneticVariation = Parts[10]; // 磁偏角
        MagneticVariationDirection = Parts[11]; // 磁偏角方向 (E/W)
        Mode = Parts[12];            // 模式 (A = Autonomous, D = Differential, E = Estimated)
        ModeStatus = Parts[13][0];


        // 返回一个对象，包含所有解析的字段
        return {
            UTC: UTC,
            Status: Status,
            Latitude: Latitude,
            LatitudeDirection: LatitudeDirection,
            Longitude: Longitude,
            LongitudeDirection: LongitudeDirection,
            Speed: Speed,
            CourseOverGround: CourseOverGround,
            _Date: _Date,
            MagneticVariation: MagneticVariation,
            MagneticVariationDirection: MagneticVariationDirection,
            Mode: Mode,
            ModeStatus: ModeStatus
        };
    } catch (e) {
        console.error("Failed to parse:", e);
        return null;
    }
}

function isROTData(Sentence) {
    return Sentence.startsWith('$GNROT,') || Sentence.startsWith('$GPROT,');
}
function parseROT(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');

        Rate = Parts[1];


        // 返回一个对象，包含所有解析的字段
        return {
            Rate: Rate,
        };
    } catch (e) {
        console.error("Failed to parse:", e);
        return null;
    }
}

function isVTGData(Sentence) {
    return Sentence.startsWith('$GNVTG,') || Sentence.startsWith('$GPVTG,');
}
function isVTGHData(Sentence) {
    return Sentence.startsWith('$GNVTGH,') || Sentence.startsWith('$GPVTGH,');
}
function parseVTG_H(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');

        // 从 Parts 数组中提取各个数据字段
        trueCourse = Parts[1];              // 地面航向（真北）
        trueCourseInd = 'T';                // 航向标志（真北），固定为 T
        magneticCourse = Parts[3];         // 地面航向（磁北）
        magneticCourseInd = 'M';            // 航向标志（磁北），固定为 M
        speedKnots = Parts[5];              // 速度（节）
        speedUnitKn = 'N';                  // 速率单位（节），固定为 N
        speedKmH = Parts[7];                // 速度（公里每小时）
        speedUnitKm = 'K';                  // 速率单位（公里/小时），固定为 K
        modeIndicator = Parts[9][0];           // 模式指示符（A = 自动, D = 差分, E = 估算）

        // 返回解析结果
        return {
            trueCourse: trueCourse,
            trueCourseInd: trueCourseInd,
            magneticCourse: magneticCourse,
            magneticCourseInd: magneticCourseInd,
            speedKnots: speedKnots,
            speedUnitKn: speedUnitKn,
            speedKmH: speedKmH,
            speedUnitKm: speedUnitKm,
            modeIndicator: modeIndicator
        };
    } catch (e) {
        console.error("Failed to parse:", e);
        return null;
    }
}

function isZDAData(Sentence) {
    return Sentence.startsWith('$GNZDA,') || Sentence.startsWith('$GPZDA,');
}
function parseZDA(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');

        utcTime = Parts[1];           // UTC 时间 (hhmmss.ss)
        day = Parts[2];               // UTC 日 (dd)
        month = Parts[3];             // UTC 月 (mm)
        year = Parts[4];              // UTC 年 (yyyy)
        localZoneHour = Parts[5];     // 本地时区的小时 (xx)
        localZoneMinute = Parts[6].slice(0,-3);   // 本地时区的分钟 (yy)

        // 返回解析结果
        return {
            utcTime: utcTime,
            day: day,
            month: month,
            year: year,
            localZoneHour: localZoneHour || 'N/A',  // 如果没有提供本地时区，设为 'N/A'
            localZoneMinute: localZoneMinute || 'N/A' // 如果没有提供本地时区，设为 'N/A'
        }
    } catch (e) {
        console.error("Failed to parse:", e);
        return null;
    }
}

function receiveDataFromAndroid(data) {
    if (isDTMData(data)) {
        var Data = parseDTM(data);
        if (Data) {
            // 更新 HTML 显示
            const dtmFields = {
                '.dtmlocalCoordinateCode': 'DatumCode',
                '.dtmlatitudeOffset': 'LatOffset',
                '.dtmlatitudeOffsetDir': 'LatDir',
                '.dtmlongitudeOffset': 'LonOffset',
                '.dtmlongitudeOffsetDir': 'LonDir',
                '.dtmAltitudeOffset': 'AltOffset',
                '.dtmRfDatumCode': 'RfDatumCode'
            };
            
            Object.keys(dtmFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[dtmFields[selector]];
            });
            
        }
    } else if (isGBSData(data)) {
        var Data = parseGBS(data);
        if (Data) {
            // 更新 HTML 显示
            const gbsFields = {
                '.gbsUTC': 'UTC',
                '.gbsLatExp': 'LatExp',
                '.gbsLonExp': 'LonExp',
                '.gbsAltExp': 'AltExp',
                '.gbsFaultySatelliteID': 'FaultySatelliteID'
            };
            
            Object.keys(gbsFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[gbsFields[selector]];
            });
            
        }
    } else if (isGGAData(data)) {
        var Data = parseGGA_H(data);
        if (Data) {
            // 更新 HTML 显示
            const ggaFields = {
                '.ggaUTC': 'UTC',
                '.ggaLat': 'Lat',
                '.ggaLatDir': 'LatDir',
                '.ggaLon': 'Lon',
                '.ggaLonDir': 'LonDir',
                '.ggaQual': 'Qual',
                '.ggaNumberOfSatellites': 'NumberOfSatellites',
                '.ggaHDOP': 'HDOP',
                '.ggaAlt': 'Alt',
                '.ggaUndulation': 'Undulation',
                '.ggaAge': 'Age',
                '.ggaID': 'ID'
            };
            
            Object.keys(ggaFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[ggaFields[selector]];
            });
            
        }
    } else if (isGGAHData(data)) {
        var Data = parseGGA_H(data);
        if (Data) {
            // 更新 HTML 显示
            const ggahFields = {
                '.ggahUTC': 'UTC',
                '.ggahLat': 'Lat',
                '.ggahLatDir': 'LatDir',
                '.ggahLon': 'Lon',
                '.ggahLonDir': 'LonDir',
                '.ggahQual': 'Qual',
                '.ggahNumberOfSatellites': 'NumberOfSatellites',
                '.ggahHDOP': 'HDOP',
                '.ggahAlt': 'Alt',
                '.ggahUndulation': 'Undulation',
                '.ggahAge': 'Age',
                '.ggahID': 'ID'
            };
            
            Object.keys(ggahFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[ggahFields[selector]];
            });
            
        }
    } else if (isGLLData(data)) {
        var Data = parseGLL_H(data);
        if (Data) {
            // 更新 HTML 显示
            const gllFields = {
                '.gllLat': 'Lat',
                '.gllLatDir': 'LatDir',
                '.gllLon': 'Lon',
                '.gllLonDir': 'LonDir',
                '.gllUTC': 'UTC',
                '.gllStatus': 'Status',
                '.gllPositioningSystemMode': 'PositioningSystemMode'
            };
            
            Object.keys(gllFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[gllFields[selector]];
            });
            
        }
    } else if (isGLLHData(data)) {
        var Data = parseGLL_H(data);
        if (Data) {
            // 更新 HTML 显示
            const gllhFields = {
                '.gllhLat': 'Lat',
                '.gllhLatDir': 'LatDir',
                '.gllhLon': 'Lon',
                '.gllhLonDir': 'LonDir',
                '.gllhUTC': 'UTC',
                '.gllhStatus': 'Status',
                '.gllhPositioningSystemMode': 'PositioningSystemMode'
            };
            
            Object.keys(gllhFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[gllhFields[selector]];
            });
            
        }
    } else if (isGNSData(data)) {
        var Data = parseGNS_H(data);
        if (Data) {
            // 更新 HTML 显示
            const gnsFields = {
                '.gnsUTC': 'UTC',
                '.gnsLat': 'Lat',
                '.gnsLatDir': 'LatDir',
                '.gnsLon': 'Lon',
                '.gnsLonDir': 'LonDir',
                '.gnsPositioningSystemMode': 'PositioningSystemMode',
                '.gnsNumberOfSatellites': 'NumberOfSatellites',
                '.gnsHDOP': 'HDOP',
                '.gnsAntAlt': 'AntAlt',
                '.gnsGeoSep': 'GeoSep',
                '.gnsAge': 'Age',
                '.gnsID': 'ID',
                '.gnsStatus': 'Status'
            };
            
            Object.keys(gnsFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[gnsFields[selector]];
            });
            
        }
    } else if (isGNSHData(data)) {
        var Data = parseGNS_H(data);
        if (Data) {
            // 更新 HTML 显示
            const gnshFields = {
                '.gnshUTC': 'UTC',
                '.gnshLat': 'Lat',
                '.gnshLatDir': 'LatDir',
                '.gnshLon': 'Lon',
                '.gnshLonDir': 'LonDir',
                '.gnshPositioningSystemMode': 'PositioningSystemMode',
                '.gnshNumberOfSatellites': 'NumberOfSatellites',
                '.gnshHDOP': 'HDOP',
                '.gnshAntAlt': 'AntAlt',
                '.gnshGeoSep': 'GeoSep',
                '.gnshAge': 'Age',
                '.gnshID': 'ID',
                '.gnshStatus': 'Status'
            };
            
            Object.keys(gnshFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[gnshFields[selector]];
            });
            
        }
    } else if (isGSTData(data)) {
        var Data = parseGST_H(data);
        if (Data) {
            // 更新 HTML 显示
            const gstFields = {
                '.gstUTC': 'UTC',
                '.gstRMS': 'RMS',
                '.gstSmjrStd': 'SmjrStd',
                '.gstSmnrStd': 'SmnrStd',
                '.gstOrient': 'Orient',
                '.gstLatStd': 'LatStd',
                '.gstLonStd': 'LonStd',
                '.gstAltStd': 'AltStd'
            };
            
            Object.keys(gstFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[gstFields[selector]];
            });
            
        }
    } else if (isGSTHData(data)) {
        var Data = parseGST_H(data);
        if (Data) {
            // 更新 HTML 显示
            const gsthFields = {
                '.gsthUTC': 'UTC',
                '.gsthRMS': 'RMS',
                '.gsthSmjrStd': 'SmjrStd',
                '.gsthSmnrStd': 'SmnrStd',
                '.gsthOrient': 'Orient',
                '.gsthLatStd': 'LatStd',
                '.gsthLonStd': 'LonStd',
                '.gsthAltStd': 'AltStd'
            };
            
            Object.keys(gsthFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[gsthFields[selector]];
            });
            
        }
    } else if (isTHSData(data)) {
        var Data = parseTHS(data);
        if (Data) {
            // 更新 HTML 显示
            document.querySelector('.thsHeading').textContent = Data.Heading;
            document.querySelector('.thsMode').textContent = Data.Mode;
        }
    } else if (isRMCData(data)) {
        var Data = parseRMC_H(data);
        if (Data) {
            // 更新 HTML 显示
            const rmcFields = {
                '.rmcUTC': 'UTC',
                '.rmcPosStatus': 'Status',
                '.rmcLat': 'Latitude',
                '.rmcLatDir': 'LatitudeDirection',
                '.rmcLon': 'Longitude',
                '.rmcLonDir': 'LongitudeDirection',
                '.rmcSpeed': 'Speed',
                '.rmcTrack': 'CourseOverGround',
                '.rmcDate': '_Date',
                '.rmcMagVar': 'MagneticVariation',
                '.rmcMagVarDir': 'MagneticVariationDirection',
                '.rmcModeInd': 'Mode',
                '.rmcModeStatus': 'ModeStatus'
            };
            
            Object.keys(rmcFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[rmcFields[selector]];
            });
            
        }
    } else if (isRMCHData(data)) {
        var Data = parseRMC_H(data);
        if (Data) {
            // 更新 HTML 显示
            const rmchFields = {
                '.rmchUTC': 'UTC',
                '.rmchPosStatus': 'Status',
                '.rmchLat': 'Latitude',
                '.rmchLatDir': 'LatitudeDirection',
                '.rmchLon': 'Longitude',
                '.rmchLonDir': 'LongitudeDirection',
                '.rmchSpeed': 'Speed',
                '.rmchTrack': 'CourseOverGround',
                '.rmchDate': '_Date',
                '.rmchMagVar': 'MagneticVariation',
                '.rmchMagVarDir': 'MagneticVariationDirection',
                '.rmchModeInd': 'Mode',
                '.rmchModeStatus': 'ModeStatus'
            };
            
            Object.keys(rmchFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[rmchFields[selector]];
            });
            
        }
    } else if (isROTData(data)) {
        var Data = parseROT(data);
        if (Data) {
            // 更新 HTML 显示
            document.querySelector('.rotRate').textContent = Data.Rate;
        }
    } else if (isVTGData(data)) {
        var Data = parseVTG_H(data);
        if (Data) {
            // 更新 HTML 显示
            const vtgFields = {
                '.vtgCourseTrue': 'trueCourse',
                '.vtgCourseIndTrue': 'trueCourseInd',
                '.vtgCourseMag': 'magneticCourse',
                '.vtgCourseIndMag': 'magneticCourseInd',
                '.vtgSpeedKn': 'speedKnots',
                '.vtgN': 'speedUnitKn',
                '.vtgSpeedKm': 'speedKmH',
                '.vtgK': 'speedUnitKm',
                '.vtgModeInd': 'modeIndicator'
            };
            
            Object.keys(vtgFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[vtgFields[selector]];
            });
            
        }
    } else if (isVTGHData(data)) {
        var Data = parseVTG_H(data);
        if (Data) {
            // 更新 HTML 显示
            const vtghFields = {
                '.vtghCourseTrue': 'trueCourse',
                '.vtghCourseIndTrue': 'trueCourseInd',
                '.vtghCourseMag': 'magneticCourse',
                '.vtghCourseIndMag': 'magneticCourseInd',
                '.vtghSpeedKn': 'speedKnots',
                '.vtghN': 'speedUnitKn',
                '.vtghSpeedKm': 'speedKmH',
                '.vtghK': 'speedUnitKm',
                '.vtghModeInd': 'modeIndicator'
            };
            
            Object.keys(vtghFields).forEach(selector => {
                document.querySelector(selector).textContent = Data[vtghFields[selector]];
            });
            
        }
    } else if (isZDAData(data)) {
        var Data = parseZDA(data);
        if (Data) {
            // 更新 HTML 显示
            const fields = {
                '.zdaUTC': 'utcTime',
                '.zdaDay': 'day',
                '.zdaMonth': 'month',
                '.zdaYear': 'year',
                '.zdaLocalZoneHour': 'localZoneHour',
                '.zdaLocalZoneMinute': 'localZoneMinute'
            };
            
            Object.keys(fields).forEach(selector => {
                document.querySelector(selector).textContent = Data[fields[selector]];
            });
        }
    }
}

const nmeaMessages = {
    'dtmContainer': 0,    // DTM: Datum Reference
    'gbsContainer': 1,    // GBS: GNSS Satellite Fault Detection
    'ggaContainer': 2,    // GGA: GPS Fix Data
    'ggahContainer': 3,   // GGAH: GPS Fix Data with Heading
    'gllContainer': 4,    // GLL: Geographic Position
    'gllhContainer': 5,   // GLLH: Geographic Position with Heading
    'gnsContainer': 6,    // GNS: GNSS Fix Data
    'gnshContainer': 7,   // GNSH: GNSS Fix Data with Heading
    'gstContainer': 8,    // GST: GNSS Pseudorange Noise Statistics
    'gsthContainer': 9,   // GSTH: GNSS Pseudorange Noise Statistics with Heading
    'thsContainer': 10,   // THS: Heading and Speed
    'rmcContainer': 11,   // RMC: Recommended Minimum Navigation Information
    'rmchContainer': 12,  // RMCH: Recommended Minimum Navigation Information with Heading
    'rotContainer': 13,   // ROT: Rate of Turn
    'vtgContainer': 14,   // VTG: Track and Ground Speed
    'vtghContainer': 15,  // VTGH: Track and Ground Speed with Heading
    'zdaContainer': 16    // ZDA: UTC Date and Time
};
function receiveDataFromAndroid_1(data) {
    // 去除 data 两端的空白字符，包括换行符
    const trimmedData = data.trim();
    scrollToContainer(nmeaMessages[trimmedData]);
}

function sendData(data) {
    Android.sendDataToAndroid(data); // 调用 Android 方法
}


