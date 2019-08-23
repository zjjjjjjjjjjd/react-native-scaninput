import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  requireNativeComponent, View, findNodeHandle, UIManager, StyleSheet
} from 'react-native';

const nativeInput = {
  name: 'NativeInput',
  propTypes: {
    placeholder: PropTypes.string,
    underline: PropTypes.boolean,
    keyboardType: PropTypes.string,
    enabled: PropTypes.boolean,
    multiline: PropTypes.boolean,
    maxLength: PropTypes.int,
    ...View.propTypes
  }
}
let NativeInput = requireNativeComponent('NativeInput', nativeInput);
export default class ScanInput extends Component {
  constructor(props) {
    super(props);

    this.value = props.value;
  }

  shouldComponentUpdate(nextProps) {
    if (nextProps.value !== this.value) {
      this.setValue(this.value = nextProps.value);
    }
    return true;
  }

  onNativeChange = (e) => {
    this.value = e;
    this.props.onChangeText && this.props.onChangeText(e.nativeEvent.text);
  }

  onNativeEnter = (e) => {
    this.props.onSubmitEditing && this.props.onSubmitEditing(e.nativeEvent.text)
  }

  setValue = (value) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.refs.NativeInput),
      UIManager.NativeInput.Commands.setValue, // Commands后面的值与原生层定义的HANDLE_METHOD_NAME一致
      [value] // 向原生层传递的参数数据,数据形如：["第一个参数","第二个参数
    )
  }

  setEnabled = (enabled) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.refs.NativeInput),
      UIManager.NativeInput.Commands.setEnabled, // Commands后面的值与原生层定义的HANDLE_METHOD_NAME一致
      [enabled] // 向原生层传递的参数数据,数据形如：["第一个参数","第二个参数
    )
  }

  blur = () => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.refs.NativeInput),
      UIManager.NativeInput.Commands.onBlur, // Commands后面的值与原生层定义的HANDLE_METHOD_NAME一致
      [] // 向原生层传递的参数数据,数据形如：["第一个参数","第二个参数
    )
  }

  focus = () => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.refs.NativeInput),
      UIManager.NativeInput.Commands.onFocus, // Commands后面的值与原生层定义的HANDLE_METHOD_NAME一致
      [] // 向原生层传递的参数数据,数据形如：["第一个参数","第二个参数
    )
  }

  render() {
    const { style = { height: 45, width: 200 }, inputStyle = { height: '100%', width: '100%' } } = this.props;
    return (
      <View style={style}>
        <NativeInput ref='NativeInput' {...this.props} style={inputStyle} onNativeChange={this.onNativeChange} onNativeEnter={this.onNativeEnter} />
      </View>)
  }
}