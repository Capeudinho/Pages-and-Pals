import React, {useContext, useEffect, useRef} from "react";
import {Routes, Route, Link} from "react-router-dom";

import scrollContext from "../context/scroll.js";

import AccountView from "../page/account/view.js";
import AccountEnter from "../page/account/enter.js";
import AccountEdit from "../page/account/edit.js";
import BookshelfView from "../page/bookshelf/view.js"
import BookshelfCreate from "../page/bookshelf/create.js";
import BookshelfEdit from "../page/bookshelf/edit.js";
import BookView from "../page/book/view.js";
import SearchBar from "../common/header/searchBar.js";
import ReviewCreate from "../page/review/create.js";
import AdvancedSearch from "../page/search/advanced.js";

import "./routing.css";

function Routing()
{
    const {scroll, setScroll} = useContext(scrollContext);
    const background = useRef();

    useEffect
    (
        () =>
        {
            setScroll({target: background?.current});
        },
        []
    );

    function handleChangeScroll(event)
    {
        setScroll(event);
    }

    return (
        <div className = "routingArea">
            <Routes>
                <Route
                exact
                path = "/"
                element =
                {
                    <div>
                        <Link to = "/account/enter">Enter </Link>
                        <Link to = "/account/view/156">Account 1 </Link>
                        <Link to = "/book/view/yjUQCwAAQBAJ"> Book Page </Link>
                    </div>
                }
                />
                <Route
                exact
                path = "/account/enter"
                element =
                {
                    <div className = "total">
                        <div className = "background enterBackground">
                            <div className = "main">
                                <AccountEnter/>
                            </div>
                        </div>
                        </div>
                }
                />
                <Route
                path = "/*"
                element =
                {
                    <div className = "total">
                        <SearchBar/>
                        <div
                        className = "background normalBackground"
                        ref = {background}
                        onScroll = {(event) => {handleChangeScroll(event)}}
                        >
                            <div className = "main">
                                <Routes>
                                    <Route exact path = "/account/view/:id" element = {<AccountView/>}/>
                                    <Route exact path = "/account/edit" element = {<AccountEdit/>}/>
                                    <Route exact path = "/bookshelf/from/:ownerId/view/:id" element = {<BookshelfView/>}/>
                                    <Route exact path = "/bookshelf/create" element = {<BookshelfCreate/>}/>
                                    <Route exact path = "/bookshelf/edit/:id" element = {<BookshelfEdit/>}/>
                                    <Route exact path = "/book/view/:apiId" element = {<BookView/>}/>
                                    <Route exact path = "/review/create" element = {<ReviewCreate/>}/>
                                    <Route exact path = "/search/advanced" element = {<AdvancedSearch/>}/>
                                </Routes>
                            </div>
                        </div>
                    </div>
                }
                />
            </Routes>
        </div>
    );
}

export default Routing;