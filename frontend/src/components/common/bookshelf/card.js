import React from "react";
import {Link} from "react-router-dom";

import BookshelfCovers from "./covers";

import "./card.css";

function BookshelfCard({bookshelf})
{
    return (
        <div className = "bookshelfCardArea">
            <Link to = {"/bookshelf/from/"+bookshelf?.owner?.id+"/view/"+bookshelf?.id}>
                <div className = "bookshelf">
                    <BookshelfCovers covers = {[bookshelf?.covers?.[0], bookshelf?.covers?.[1], bookshelf?.covers?.[2]]}/>
                    <div className = "bookshelfInfo">
                        <div className = "bookshelfName">
                            {bookshelf?.name}
                        </div>
                        <div className = "bookshelfDescription">
                            {bookshelf?.description}
                        </div>
                    </div>
                </div>
            </Link>
        </div>
    );
}

export default BookshelfCard;