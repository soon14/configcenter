export function timestampToENDate(timestamp) {
    if(timestamp){
      let newDate = new Date(timestamp);
      return newDate.toLocaleDateString();
    }
    return '';
}

export function timestampToENDateCopy(timestamp) {
    if(timestamp){
        let newDate = new Date(timestamp);
        let dateString = newDate.toLocaleDateString();
        let date_array = dateString.split('/');
        if(date_array && date_array[1] && date_array[1].length === 1){
            date_array[1] = '0' + date_array[1];
        }
        if(date_array && date_array[2] &&date_array[2].length === 1){
            date_array[2] = '0' + date_array[2];
        }
        dateString = date_array[0] + '/' +  date_array[1] + '/' +  date_array[2]
        return dateString;
    }
    return '';
}

export function timestampToFullDate(timestamp) {
    if(timestamp){
        let newDate = new Date(timestamp);
        let dateString = newDate.toLocaleDateString();
        let time = newDate.toTimeString();
        let index_ = time.indexOf(' ');
        let allTimeString = time.substr(0,index_);
        return dateString+' '+allTimeString;
    }
    return '';
}

export function datestampToChinaString(datestamp) {
    if(typeof datestamp !== 'string'){
        let date_string = new Date(datestamp).toLocaleDateString();
        let date_array = date_string.split('/');
        if(date_array[1] && date_array[1].length<2){
            date_array[1] = '0'+ date_array[1];
        }
        if(date_array[2] && date_array[2].length<2){
            date_array[2] = '0'+ date_array[2];
        }
        date_string = date_array[0]+'年'+date_array[1]+'月'+date_array[2]+'日';
        return date_string;
    }
    return datestamp;
}
