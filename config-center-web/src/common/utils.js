export function getCurrentMenu (location, arrayMenu) {
  if (!!arrayMenu) {
    let current = [];
    for (let i = 0; i < arrayMenu.length; i++) {
      const e = arrayMenu[ i ];
      const child = getCurrentMenu(location, e.children);
      if (!!child && child.length > 0) {
        child.push({ ...e, children: null });
        return child;
      }
      if (e.href && pathToRegexp(e.href).exec(location)) {
        current.push({ ...e, children: null });
        return current;
      }
    }
    return current;
  }
  return null;
}

//根据key找到对应的值
export function getValueByKey(key, array) {
  for(let i=0;i<array.length;i++) {
    if(array[i].value == key) {
      return array[i].text;
    }
  }
}