import React, {useContext} from "react";
import {Routes, Route, Link} from "react-router-dom";

import AccountView from "../page/account/view.js";
import AccountEnter from "../page/account/enter.js";
import AccountEdit from "../page/account/edit.js";
import BookshelfView from "../page/bookshelf/view.js"
import BookshelfCreate from "../page/bookshelf/create.js";
import BookshelfEdit from "../page/bookshelf/edit.js";
import BookView from "../page/book/view.js";

import "./routing.css";

function Routing()
{
    return (
        <div className = "area routingArea">
            <Routes>
                <Route
                exact
                path = "/"
                element =
                {
                    <div>
                        <Link to = "/account/enter">Enter </Link>
                        <Link to = "/account/view/1">Account 1 </Link>
                        <Link to = "/account/view/2">Account 2 </Link>
                    </div>
                }
                />
                <Route
                exact
                path = "/account/enter"
                element =
                {
                    <div className = "enterBackground">
                        <div className = "main">
                            <AccountEnter/>
                        </div>
                    </div>
                }
                />
                <Route
                path = "/*"
                element =
                {
                    <div className = "otherBackground">
                        <div className = "main">
                            <Routes>
                                <Route exact path = "/account/view/:id" element = {<AccountView/>}/>
                                <Route exact path = "/account/edit" element = {<AccountEdit/>}/>
                                <Route exact path = "/bookshelf/from/:ownerId/view/:id" element = {<BookshelfView/>}/>
                                <Route exact path = "/bookshelf/create" element = {<BookshelfCreate/>}/>
                                <Route exact path = "/bookshelf/edit/:id" element = {<BookshelfEdit/>}/>
                                <Route exact path = "/book/view/:apiId" element = {<BookView/>}/>
                            </Routes>
                        </div>
                    </div>
                }
                />
            </Routes>
        </div>
    );
}

export default Routing;