import React, {useState, useEffect, useContext} from "react";
import {useHref} from "react-router-dom";

import alertContext from "../../context/alert.js";
import confirmContext from "../../context/confirm.js";
import overlayContext from "../../context/overlay.js";

import "./list.css";

function AlertList()
{
    const {alert, setAlert} = useContext(alertContext);
    const {confirm, setConfirm} = useContext(confirmContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const [alertList, setAlertList] = useState([]);
    const [confirmList, setConfirmList] = useState([]);
    const href = useHref();

    useEffect
    (
        () =>
        {
            if (alert instanceof Array)
            {
                var newAlertList = [...alertList];
                for (var alertIndex = 0; alertIndex < alert?.length; alertIndex++)
                {
                    var newKey = 0;
                    for (var alertListIndex = 0; alertListIndex < alertList?.length; alertListIndex++)
                    {
                        if (alertListIndex === 0 || alertListIndex?.[alertListIndex]?.key > newKey)
                        {
                            newKey = alertList?.[alertListIndex]?.key;
                        }
                    }
                    newKey = newKey+alertIndex+1;
                    alert[alertIndex].key = newKey;
                    newAlertList?.unshift(...alert);
                }
                setAlertList(newAlertList);
            }
        },
        [alert]
    );

    useEffect
    (
        () =>
        {
            if (confirm instanceof Array)
            {
                var newConfirmList = [...confirmList];
                for (var confirmIndex = 0; confirmIndex < confirm?.length; confirmIndex++)
                {
                    var include = true;
                    var newKey = 0;
                    for (var confirmListIndex = 0; confirmListIndex < confirmList?.length; confirmListIndex++)
                    {
                        if (confirm?.[confirmIndex]?.identifier === confirmList?.[confirmListIndex]?.identifier)
                        {
                            include = false;
                        }
                        else if (confirmListIndex === 0 || confirmListIndex?.[confirmListIndex]?.key > newKey)
                        {
                            newKey = confirmList?.[confirmListIndex]?.key;
                        }
                    }
                    if (include)
                    {
                        newKey = newKey+confirmIndex+1;
                        confirm[confirmIndex].key = newKey;
                        newConfirmList?.unshift(...confirm);
                    }
                }
                setConfirmList(newConfirmList);
            }
        },
        [confirm]
    );

    useEffect
    (
        () =>
        {
            setConfirm(null);
            setConfirmList([]);
        },
        [href]
    );

    function removeAlert(index)
    {
        var newAlertList = [...alertList];
        newAlertList?.splice(index, 1);
        setAlertList(newAlertList);
    }

    function respondConfirm(confirmIndex, optionIndex)
    {
        setConfirm(confirmList?.[confirmIndex]?.options?.[optionIndex]?.type);
        var newConfirms = [...confirmList];
        newConfirms?.splice(confirmIndex, 1);
        setConfirmList(newConfirms);
    }

    return (
        <div className = "alertListArea">
            {
                alertList?.length > 0 || confirmList?.length > 0 ?
                <div className = "alertList">
                    {
                        confirmList?.map
                        (
                            (confirmItem, confirmIndex) =>
                            {
                                return (
                                    <div
                                    className = "confirm clickSensitive"
                                    key = {confirmItem?.key}
                                    >
                                        <div className = "confirmText">{confirmItem?.text}</div>
                                        {
                                            confirmItem?.options?.map
                                            (
                                                (option, optionIndex) =>
                                                {
                                                    return (
                                                        <button
                                                        className = "confirmButton"
                                                        key = {optionIndex}
                                                        onClick = {() => {respondConfirm(confirmIndex, optionIndex)}}
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
                        )
                    }
                    {
                        alertList?.map
                        (
                            (alertItem, index) =>
                            {
                                return (
                                    <div
                                    className = {"alert "+alertItem?.type}
                                    key = {alertItem?.key}
                                    onAnimationEnd = {() => {removeAlert(index)}}
                                    >
                                        {alertItem?.text}
                                    </div>
                                );
                            }
                        )
                    }
                </div> :
                <></>
            }
        </div>
    );
}

export default AlertList;