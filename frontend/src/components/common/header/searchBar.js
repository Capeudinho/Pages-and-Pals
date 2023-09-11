import React, { useState, useEffect, useContext } from "react";
import { Link, useNavigate, useHref } from "react-router-dom";

import loggedAccountContext from "../../context/loggedAccount.js";
import alertContext from "../../context/alert.js";
import confirmContext from "../../context/confirm.js";
import clickContext from "../../context/click.js";

import "./searchBar.css";


function SearchBar() {
    const { loggedAccount, setLoggedAccount } = useContext(loggedAccountContext);
    const [showOptions, setShowOptions] = useState(false);
    const { setAlert } = useContext(alertContext);
    const { confirm, setConfirm } = useContext(confirmContext);
    const {click, setClick} = useContext(clickContext);
    const [searchTerm, setSearchTerm] = useState("");
    const navigate = useNavigate();
    const href = useHref();

    useEffect
    (
        () =>
        {
            if (showOptions && click?.target?.closest(".clickSensitive") === null) {
                setShowOptions(false);
            }
            else if (!showOptions && click?.target?.closest(".clickResponsiveAccount") !== null ) {
                setShowOptions(true);
            }
        },
        [click]
    );

    useEffect
    (
        () => {
            let mounted = true;
            const runEffect = async () => {
                if (confirm === "logout") {
                    setConfirm(null);
                    handleLogOut();
                }
            }
            runEffect();
            return (() => { mounted = false });
        },
        [confirm]
    );

    useEffect
    (
        () =>
        {
            setShowOptions(false);
        },
        [href]
    );

    const handleSearch = async () => {
        var newSearch = "?";
        if (searchTerm !== "") {
            newSearch = newSearch + "term=" + searchTerm + "&resulttype=all";
            navigate("/search/results" + newSearch);
        }
        else{
            setAlert([{type: "warning", text: "Empty filter."}]);
        }
    }

    function handleConfirmLogOut() {
        setConfirm([{ identifier: "logout", text: "Log out from account?", options: [{ type: "logout", text: "Log out" }, { type: "cancel", text: "Cancel" }] }]);
    }

    function handleLogOut() {
        localStorage.clear();
        setAlert([{ type: "success", text: "Logged out from account." }]);
        setLoggedAccount(null);
        navigate("/");
    }

    return (
        <div className="searchBarArea">
            <div className="leftBox">
                <Link
                    to={"/"}>
                    <img
                        className="logo"
                        src={process.env.PUBLIC_URL + "/logo.svg"}
                    />
                </Link>
            </div>
            <div className="middleBox">
                <div className="searchBox">
                    <div className="label">Search books, and bookshelves</div>
                    <div className="inputGroup">
                        <input type="text"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        spellCheck={false}
                        />
                        <button
                        className="searchButton"
                        onClick={handleSearch}>
                            Search
                        </button>
                    </div>
                </div>
                <Link
                className="advancedSearchButton"
                to="/search/advanced"
                >
                    Advanced
                </Link>
            </div>
            <div className="rightBox">
                {
                    loggedAccount?.id !== undefined ?
                    <>
                        <div
                        className="picture clickResponsiveAccount"
                        style={{ backgroundImage: "url("+loggedAccount?.picture+")"}}
                        />
                        {
                            showOptions ?
                            <div className="options clickSensitive">
                                <div>{loggedAccount?.name}</div>
                                <Link
                                className="option"
                                to={"/account/view/"+loggedAccount?.id}
                                >
                                    Account page
                                </Link>
                                <Link
                                className="option"
                                to="/account/edit">
                                    Edit account
                                </Link>
                                <div
                                className="option"
                                onClick={handleConfirmLogOut}
                                >
                                    Log out
                                </div>
                            </div> :
                            <></>
                        }
                    </> :
                    <Link
                    className="register"
                    to={"/account/enter"}>
                        Register
                    </Link>
                }
            </div>
        </div>
    );
}
export default SearchBar;