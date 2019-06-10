# 目的

> 此插件封装原生安卓的输入框组件(EditText),并且禁用了系统软键盘，为的是适用PDA手持项目需求（有硬键盘的安卓手机）。个人原因由于安卓方面不是特别懂，可能会存在一些bug。欢迎各位提issues！

# 已知问题

##### 由于此组件为原生输入框组件，所以在与react-native-navigation配合使用时候，会发生切换（push）到下一个路由页面但是上一个页面的输入监听还存在的情况，这个是由于react-native-navigation使用的并不是原生的activity来做路由切换，只是类似于一层组件覆盖,所以使用的时候请注意，遇到类似问题可以参考使用以下两种方案解决：

> 1. 避免使用push，尽量使用reset
> 2. 利用react-native-navigation的全局路由监听和组件方法setEnabled分别控制某个页面上的输入框是否可以输入
> 如果有更好的解决方案欢迎提PR

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
import ScanInput from 'react-native-scaninput';
```
## API

| 参数 | 说明 | 类型 | 默认值 |
| ---- | :-------------: | -----: | ------: |
| placeholder | 如果没有任何文字输入，会显示此字符串。 | String | - |
| underline | 文本框的下划线是否显示 | Boolean | - |
| keyboardType | 输入的类型控制可选  numeric(数字), phone（电话）, password（密码）, decimal（小数点）, normal（默认） | String | normal |
| enabled | 是否可以输入 | Boolean | true |
| multiline | 如果为true，文本框中可以输入多行文字。 | Boolean | false |
| maxLength | 最多输入长度 | Int | - |
| onChangeText | 当文本框内容变化时调用此回调函数。改变后的文字内容会作为参数传递。 | Function(value) | - |
| onSubmitEditing | 此回调函数当软键盘的确定/提交按钮被按下的时候调用此函数 | Function(value) | - |

## method
| 参数 | 说明 | 类型 | 默认值 |
| ---- | :-------------: | -----: | ------: |
| setValue | 设置value | Function | - |
| setEnabled | 设置是否可用 | Function | - |
| blur | 失去焦点 | Function | - |
| focus | 聚集 | Function | - |