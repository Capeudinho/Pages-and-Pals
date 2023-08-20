import React, {useContext} from "react";

import overlayContext from "../../context/overlay.js";

import "./group.css";

function ButtonGroup({options})
{
    const {overlay, setOverlay} = useContext(overlayContext);

    return (
        <div className = "buttonGroupArea">
            {
                options?.map
                (
                    (option, index) =>
                    {
                        return (
                            <button
                            className = "optionButton"
                            key = {index}
                            onClick = {() => {option?.operation()}}
                            disabled = {overlay}
                            >
                                {option?.text}
                            </button>
                        );
                    }
                )
            }
        </div>                
    );
}

export default ButtonGroup;