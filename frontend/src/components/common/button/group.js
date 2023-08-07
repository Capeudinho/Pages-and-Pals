import React from "react";

import "./group.css";

function ButtonGroup({options})
{
    return (
        <div className = "buttonGroupArea">
            {
                options?.map
                (
                    (option, index) =>
                    {
                        return (
                            <button
                            className = {"optionButton"}
                            key = {index}
                            onClick = {() => {option?.operation()}}
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