import React from "react";

import "./mode.css";

function ButtonMode({modes, currentMode, setMode, appearance})
{
    return (
        <div className = {"buttonModeArea "+(appearance === "switch" ? "switch" : "default")}>
            <div className = "buttons">
                {
                    modes?.map
                    (
                        (mode, index) =>
                        {
                            return (
                                <button
                                className = {"modeButton "+(currentMode === mode?.type ? "selected" : "unselected")}
                                key = {index}
                                onClick = {() => {setMode(mode?.type)}}
                                >
                                    {mode?.text}
                                </button>
                            );
                        }
                    )
                }
            </div>
        </div>                
    );
}

export default ButtonMode;