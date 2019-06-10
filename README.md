
# react-native-scaninput

## Getting started

`$ npm install react-native-scaninput --save`

### Mostly automatic installation

`$ react-native link react-native-scaninput`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.scaninput.RNScaninputPackage;` to the imports at the top of the file
  - Add `new RNScaninputPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-scaninput'
  	project(':react-native-scaninput').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-scaninput/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-scaninput')
  	```


## Usage
```javascript
import RNScaninput from 'react-native-scaninput';

// TODO: What to do with the module?
RNScaninput;
```
  