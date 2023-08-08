import React, {useState, useEffect, useContext, useRef} from "react";
import {useNavigate} from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import alertContext from "../../context/alert.js";
import confirmContext from "../../context/confirm.js";
import overlayContext from "../../context/overlay.js";

import ButtonGroup from "../../common/button/group.js";
import ButtonMode from "../../common/button/mode.js";

import "./edit.css";

function AccountEdit()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {confirm, setConfirm} = useContext(confirmContext);
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
        []
    );

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                if (confirm === "discard")
                {
                    setConfirm(null);
                    handleDiscard();
                }
            }
            runEffect();
            return (() => {mounted = false});
        },
        [confirm]
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

    function handleChangePrivacy(newPrivacy)
    {
        var newAccount = {...account};
        newAccount.privacy = newPrivacy;
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

    function handleConfirmDiscard()
    {
        setConfirm([{identifier: "discard", text: "Discard alterations from account?", options:[{type: "discard", text: "Discard"}, {type: "cancel", text: "Cancel"}]}]);
    }

    async function handleSave()
    {
        try
        {
            setOverlay(true);
            var response = await api.patch
            (
                "/account/update",
                account,
                {
                    headers:
                    {
                        email: loggedAccount?.email,
                        password: loggedAccount?.password
                    }
                }
            );
            setOverlay(false);
            localStorage.setItem("account", JSON.stringify(response?.data));
            setLoggedAccount(response?.data);
            setAlert([{type: "success", text: "Account saved."}]);
            navigate("/account/view/"+loggedAccount?.id);
        }
        catch (exception)
        {
            setOverlay(true);
            if (exception?.response?.data === "incorrect id")
            {
                localStorage.clear();
                setLoggedAccount("none");
                navigate("/");
            }
            else if (exception?.response?.data === "email taken")
            {
                setAlert([{type: "warning", text: "E-mail is taken."}]);
            }
            else if (exception?.response?.data === "invalid name")
            {
                setAlert([{type: "warning", text: "Name is invalid."}]);
            }
            else if (exception?.response?.data === "invalid email")
            {
                setAlert([{type: "warning", text: "E-mail is invalid."}]);
            }
            else if (exception?.response?.data === "invalid password")
            {
                setAlert([{type: "warning", text: "Password is invalid."}]);
            }
            else
            {
                navigate("/");
            }
        }
        setOverlay(false);
    }

    async function handleDiscard()
    {
        navigate("/account/view/"+loggedAccount.id);
    }

    return (
        <div className = "page accountEditArea">
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
            <ButtonMode
            modes = {[{text: "Public", type: true}, {text: "Private", type: false}]}
            currentMode = {account?.privacy}
            setMode = {handleChangePrivacy}
            appearance = "switch"
            />
            <div className = "label">Link to profile picture</div>
            <input
            className = "normalInput"
            value = {account?.picture}
            onChange = {(event) => {handleChangePicture(event)}}
            placeholder = "Optional"
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
            <ButtonGroup options = {[{text: "Save", operation: handleSave}, {text: "Discard", operation: handleConfirmDiscard}]}/>
        </div>
    );
}

export default AccountEdit;