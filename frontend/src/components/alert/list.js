import React, {useState, useEffect, useContext} from "react";

import alertContext from "../context/alert.js";

import "./list.css";

function AlertList()
{
    const {alert, setAlert} = useContext(alertContext);
    const [alerts, setAlerts] = useState([]);

    useEffect
    (
        () =>
        {
            if (alert.length > 0)
            {
                var newAlerts = [...alerts];
                newAlerts.unshift(...alert);
                setAlerts(newAlerts);
            }
        },
        [alert]
    );

    function removeAlert (index)
    {
        var newAlerts = [...alerts];
        newAlerts.splice(index, 1);
        setAlerts(newAlerts);
        if (newAlerts.length === 0)
        {
            setAlert([]);
        }
    }

    return (
        <div className = "alertListArea">
            <div className = "alertList">
                {
                    alerts.map
                    (
                        (alert, index) =>
                        {
                            return (
                                <div
                                className = "alert"
                                key = {alerts[alerts.length-1-index].key}
                                onAnimationEnd = {() => {removeAlert(alerts.length-1-index)}}
                                style =
                                {
                                    {
                                        borderColor:
                                        alerts[alerts.length-1-index].type === "success" ? "#a3cca3" :
                                        alerts[alerts.length-1-index].type === "warning" ? "#cccca3" :
                                        alerts[alerts.length-1-index].type === "error" ? "#cca3a3" :
                                        "#cccccc",
                                        backgroundColor:
                                        alerts[alerts.length-1-index].type === "success" ? "#e6ffe6" :
                                        alerts[alerts.length-1-index].type === "warning" ? "#ffffe6" :
                                        alerts[alerts.length-1-index].type === "error" ? "#ffe6e6" :
                                        "#e6e6e6",
                                        color:
                                        alerts[alerts.length-1-index].type === "success" ? "#0a330a" :
                                        alerts[alerts.length-1-index].type === "warning" ? "#33330a" :
                                        alerts[alerts.length-1-index].type === "error" ? "#330a0a" :
                                        "#333333"
                                    }
                                }
                                >
                                    {alerts[alerts.length-1-index].text}
                                </div>
                            );
                        }
                    )
                }
            </div>
        </div>
    );
}

export default AlertList;