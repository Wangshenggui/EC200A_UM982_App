
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
function parseGGA(Sentence) {
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
        AltUnits = Parts[10];
        Undulation = Parts[11];
        UndulationUnits = Parts[12];
        Age = Parts[13];
        ID = Parts[14];


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
            AltUnits: AltUnits,
            Undulation: Undulation,
            UndulationUnits: UndulationUnits,
            Age: Age,
            ID: ID,
        };
    } catch (e) {
        console.error("Failed to parse:", e);
        return null;
    }
}

function isGGAHData(Sentence) {
    return Sentence.startsWith('$GNGGAH,') || Sentence.startsWith('$GPGGAH,');
}
function parseGGAH(Sentence) {
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
        AltUnits = Parts[10];
        Undulation = Parts[11];
        UndulationUnits = Parts[12];
        Age = Parts[13];
        ID = Parts[14];


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
            AltUnits: AltUnits,
            Undulation: Undulation,
            UndulationUnits: UndulationUnits,
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
function parseGLL(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');
        
        Lat = Parts[1];
        LatDir = Parts[2];
        Lon = Parts[3];
        LonDir = Parts[4];
        UTC = Parts[5];
        Status = Parts[6];
        PositioningSystemMode = Parts[7];


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

function isGLLHData(Sentence) {
    return Sentence.startsWith('$GNGLLH,') || Sentence.startsWith('$GPGLLH,');
}
function parseGLLH(Sentence) {
    try {
        // 去除 $ 符号
        var Parts = Sentence.split('$')[1].split(',');
        
        Lat = Parts[1];
        LatDir = Parts[2];
        Lon = Parts[3];
        LonDir = Parts[4];
        UTC = Parts[5];
        Status = Parts[6];
        PositioningSystemMode = Parts[7];


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

function receiveDataFromAndroid(data) {
    if (isDTMData(data)) {
        var Data = parseDTM(data);
        if (Data) {
            // 更新 HTML 显示
            document.querySelector('.dtmlocalCoordinateCode').textContent = Data.DatumCode;
            document.querySelector('.dtmlatitudeOffset').textContent = Data.LatOffset;
            document.querySelector('.dtmlatitudeOffsetDir').textContent = Data.LatDir;
            document.querySelector('.dtmlongitudeOffset').textContent = Data.LonOffset;
            document.querySelector('.dtmlongitudeOffsetDir').textContent = Data.LonDir;
            document.querySelector('.dtmAltitudeOffset').textContent = Data.AltOffset;
            document.querySelector('.dtmRfDatumCode').textContent = Data.RfDatumCode;
        }
    } else if (isGBSData(data)) {
        var Data = parseGBS(data);
        if (Data) {
            // 更新 HTML 显示
            document.querySelector('.gbsUTC').textContent = Data.UTC;
            document.querySelector('.gbsLatExp').textContent = Data.LatExp;
            document.querySelector('.gbsLonExp').textContent = Data.LonExp;
            document.querySelector('.gbsAltExp').textContent = Data.AltExp;
            document.querySelector('.gbsFaultySatelliteID').textContent = Data.FaultySatelliteID;
        }
    } else if (isGGAData(data)) {
        var Data = parseGGA(data);
        if (Data) {
            // 更新 HTML 显示
            document.querySelector('.ggaUTC').textContent = Data.UTC;
            document.querySelector('.ggaLat').textContent = Data.Lat;
            document.querySelector('.ggaLatDir').textContent = Data.LatDir;
            document.querySelector('.ggaLon').textContent = Data.Lon;
            document.querySelector('.ggaLonDir').textContent = Data.LonDir;
            document.querySelector('.ggaQual').textContent = Data.Qual;
            document.querySelector('.ggaNumberOfSatellites').textContent = Data.NumberOfSatellites;
            document.querySelector('.ggaHDOP').textContent = Data.HDOP;
            document.querySelector('.ggaAlt').textContent = Data.Alt;
            document.querySelector('.ggaAltUnits').textContent = Data.AltUnits;
            document.querySelector('.ggaUndulation').textContent = Data.Undulation;
            document.querySelector('.ggaUndulationUnits').textContent = Data.UndulationUnits;
            document.querySelector('.ggaAge').textContent = Data.Age;
            document.querySelector('.ggaID').textContent = Data.ID;
        }
    } else if (isGGAHData(data)) {
        var Data = parseGGAH(data);
        if (Data) {
            // 更新 HTML 显示
            document.querySelector('.ggahUTC').textContent = Data.UTC;
            document.querySelector('.ggahLat').textContent = Data.Lat;
            document.querySelector('.ggahLatDir').textContent = Data.LatDir;
            document.querySelector('.ggahLon').textContent = Data.Lon;
            document.querySelector('.ggahLonDir').textContent = Data.LonDir;
            document.querySelector('.ggahQual').textContent = Data.Qual;
            document.querySelector('.ggahNumberOfSatellites').textContent = Data.NumberOfSatellites;
            document.querySelector('.ggahHDOP').textContent = Data.HDOP;
            document.querySelector('.ggahAlt').textContent = Data.Alt;
            document.querySelector('.ggahAltUnits').textContent = Data.AltUnits;
            document.querySelector('.ggahUndulation').textContent = Data.Undulation;
            document.querySelector('.ggahUndulationUnits').textContent = Data.UndulationUnits;
            document.querySelector('.ggahAge').textContent = Data.Age;
            document.querySelector('.ggahID').textContent = Data.ID;
        }
    } else if (isGLLData(data)) {
        var Data = parseGLL(data);
        if (Data) {
            // 更新 HTML 显示
            document.querySelector('.gllLat').textContent = Data.Lat;
            document.querySelector('.gllLatDir').textContent = Data.LatDir;
            document.querySelector('.gllLon').textContent = Data.Lon;
            document.querySelector('.gllLonDir').textContent = Data.LonDir;
            document.querySelector('.gllUTC').textContent = Data.UTC;
            document.querySelector('.gllStatus').textContent = Data.Status;
            document.querySelector('.gllPositioningSystemMode').textContent = Data.PositioningSystemMode;
        }
    } else if (isGLLHData(data)) {
        var Data = parseGLLH(data);
        if (Data) {
            // 更新 HTML 显示
            document.querySelector('.gllhLat').textContent = Data.Lat;
            document.querySelector('.gllhLatDir').textContent = Data.LatDir;
            document.querySelector('.gllhLon').textContent = Data.Lon;
            document.querySelector('.gllhLonDir').textContent = Data.LonDir;
            document.querySelector('.gllhUTC').textContent = Data.UTC;
            document.querySelector('.gllhStatus').textContent = Data.Status;
            document.querySelector('.gllhPositioningSystemMode').textContent = Data.PositioningSystemMode;
        }
    }
}

function sendData(data) {
    Android.sendDataToAndroid(data); // 调用 Android 方法
}
