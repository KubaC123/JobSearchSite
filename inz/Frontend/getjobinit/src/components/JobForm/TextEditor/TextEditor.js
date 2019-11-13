import React, { Component } from 'react';
import MUIRichTextEditor from "mui-rte";
import Divider from '@material-ui/core/Divider';

class TextEditor extends Component {

  render() {

    let controls = ["title", "bold", "italic", "underline", "undo", "redo", "bulletList", "save"];

    return (
      <div>
        <Divider />
          <MUIRichTextEditor 
            label={this.props.label}
            onChange={this.props.textChanged}
            controls={controls}/>
        <Divider />
      </div>
    )
  }
}

export default TextEditor;