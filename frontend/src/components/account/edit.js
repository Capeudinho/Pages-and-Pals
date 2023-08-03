import React, {useState, useEffect, useContext, useRef} from "react";
import {useNavigate} from "react-router-dom";
import api from "../../services/api.js";

import loggedAccountContext from "../context/loggedAccount.js";
import alertContext from "../context/alert.js";
import overlayContext from "../context/overlay.js";

import "./edit.css";

function AccountEdit()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const [account, setAccount] = useState
    (
        {
            name: "",
            email: "",
            password: "",
            biography: "",
            privacy: false,
            picture: ""
        }
    );
    const [showPassword, setShowPassword] = useState(false);
    const biographyInput = useRef();
    const navigate = useNavigate();

    useEffect
    (
        () =>
        {
            if (loggedAccount?.id !== undefined)
            {
                setAccount(loggedAccount);
                biographyInput.current.innerText = loggedAccount?.biography;
            }
            else
            {
                navigate("/");
            }
        },
        [loggedAccount]
    );

    function handleChangeName(event)
    {
        var newAccount = {...account};
        newAccount.name = event.target.value;
        setAccount(newAccount);
    }

    function handleChangeEmail(event)
    {
        var newAccount = {...account};
        newAccount.email = event.target.value;
        setAccount(newAccount);
    }

    function handleChangePassword(event)
    {
        var newAccount = {...account};
        newAccount.password = event.target.value;
        setAccount(newAccount);
    }

    function handleChangeBiography(event)
    {
        var newAccount = {...account};
        newAccount.biography = event.target.innerText;
        setAccount(newAccount);
    }

    function handleChangePrivacyPublic()
    {
        var newAccount = {...account};
        newAccount.privacy = true;
        setAccount(newAccount);
    }

    function handleChangePrivacyPrivate()
    {
        var newAccount = {...account};
        newAccount.privacy = false;
        setAccount(newAccount);
    }

    function handleChangePicture(event)
    {
        var newAccount = {...account};
        newAccount.picture = event.target.value;
        setAccount(newAccount);
    }

    function handleChangeShowPassword()
    {
        setShowPassword(!showPassword);
    }

    async function handleSave()
    {
        setOverlay(true);
        try
        {
            const response = await api.patch
            (
                "/account/update",
                account
            );
            localStorage.setItem("account", JSON.stringify(response?.data));
            setLoggedAccount(response?.data);
            setAlert([{text: "Account saved.", type: "success", key: Math.random()}]);
            navigate("/account/view/"+loggedAccount?.id);
        }
        catch (exception)
        {
            if (exception?.response?.data === "incorrect id")
            {
                localStorage.clear();
                setLoggedAccount("none");
                navigate("/");
            }
            else if (exception?.response?.data === "email taken")
            {
                setAlert([{text: "E-mail is taken.", type: "warning", key: Math.random()}]);
            }
            else if (exception?.response?.data === "invalid name")
            {
                setAlert([{text: "Name is invalid.", type: "warning", key: Math.random()}]);
            }
            else if (exception?.response?.data === "invalid email")
            {
                setAlert([{text: "E-mail is invalid.", type: "warning", key: Math.random()}]);
            }
            else if (exception?.response?.data === "invalid password")
            {
                setAlert([{text: "Password is invalid.", type: "warning", key: Math.random()}]);
            }
        }
        setOverlay(false);
    }

    async function handleCancel()
    {
        navigate("/account/view/"+loggedAccount.id);
    }

    return (
        <div className = "area accountEditArea">
            <div className = "mainBox">
                <div className = "label">Name</div>
                <input
                className = "normalInput"
                value = {account?.name}
                onChange = {(event) => {handleChangeName(event)}}
                spellCheck = {false}
                />
                <div className = "label">E-mail</div>
                <input
                className = "normalInput"
                value = {account?.email}
                onChange = {(event) => {handleChangeEmail(event)}}
                spellCheck = {false}
                />
                <div className = "label">Password</div>
                <div className = "passwordBox">
                    <input
                    className = "passwordInput"
                    value = {account?.password}
                    onChange = {(event) => {handleChangePassword(event)}}
                    type = {showPassword ? "text" : "password"}
                    spellCheck = {false}
                    />
                    <button
                    className = "showPasswordButton"
                    onClick = {() => {handleChangeShowPassword()}}
                    >
                        {showPassword ? "H" : "S"}
                    </button>
                </div>
                <div className = "label">Biography</div>
                <div
                className = "biographyInput"
                ref = {biographyInput}
                onInput = {(event) => {handleChangeBiography(event)}}
                spellCheck = {false}
                contentEditable = {true}
                placeholder = "Optional"
                />
                <div className = "label">Privacy</div>
                <div className = "privacyBox">
                    <button
                    className = "privacyButton privacyPublicButton"
                    onClick = {() => {handleChangePrivacyPublic()}}
                    style = {{backgroundColor: account?.privacy ? "#ffffff" : "#cccccc"}}
                    >
                        Public
                    </button>
                    <button
                    className = "privacyButton privacyPrivateButton"
                    onClick = {() => {handleChangePrivacyPrivate()}}
                    style = {{backgroundColor: !account?.privacy ? "#ffffff" : "#cccccc"}}
                    >
                        Private
                    </button>
                </div>
                <div className = "label">Link to profile picture</div>
                <input
                className = "normalInput"
                value = {account?.picture}
                onChange = {(event) => {handleChangePicture(event)}}
                spellCheck = {false}
                />
                <div className = "pictureBox">
                    <div
                    className = "picture bigPicture"
                    style = {{backgroundImage: "url("+account?.picture+")"}}
                    />
                    <div
                    className = "picture smallPicture"
                    style = {{backgroundImage: "url("+account?.picture+")"}}
                    />
                </div>
                <div className = "buttonBox">
                    <button
                    className = "completeButton"
                    onClick = {() => {handleSave()}}
                    >
                        Save
                    </button>
                    <button
                    className = "completeButton"
                    onClick = {() => {handleCancel()}}
                    >
                        Cancel
                    </button>
                </div>
            </div>
        </div>
    );
}

export default AccountEdit;