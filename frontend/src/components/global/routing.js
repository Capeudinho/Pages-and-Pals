import React from "react";
import {BrowserRouter, Routes, Route, Link} from "react-router-dom";

import AccountView from "../account/view.js";
import AccountEnter from "../account/enter.js";
import AccountEdit from "../account/edit.js";
import BookshelfView from "../bookshelf/view.js"
import BookshelfCreate from "../bookshelf/create.js";
import BookshelfEdit from "../bookshelf/edit.js";

import "./routing.css";

function Routing()
{
    return (
        <div className = "area routingArea">
            <BrowserRouter>
                <Routes>
                    <Route
                    exact
                    path = "/"
                    element =
                    {
                        <div>
                            <Link to = "/account/enter">Enter</Link>
                            <Link to = "/account/view/1">Account 1</Link>
                            <Link to = "/account/view/2">Account 2</Link>
                            <Link to = "/bookshelf/create">Create bookshelf</Link>
                        </div>
                    }
                    />
                    <Route exact path = "/account/view/:id" element = {<AccountView/>}/>
                    <Route exact path = "/account/enter" element = {<AccountEnter/>}/>
                    <Route exact path = "/account/edit" element = {<AccountEdit/>}/>
                    <Route exact path = "/bookshelf/from/:ownerId/view/:id" element = {<BookshelfView/>}/>
                    <Route exact path = "/bookshelf/create" element = {<BookshelfCreate/>}/>
                    <Route exact path = "/bookshelf/edit/:id" element = {<BookshelfEdit/>}/>
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default Routing;