import React from "react";

import "./covers.css";

function BookshelfCovers({covers})
{
    return (
        <div className = "bookshelfCoversArea">
            <div
            className = "cover coverOne"
            style = {{backgroundImage: "url("+covers?.[0]+")"}}
            />
            <div
            className = "cover coverTwo"
            style = {{backgroundImage: "url("+covers?.[1]+")"}}
            />
            <div
            className = "cover coverThree"
            style = {{backgroundImage: "url("+covers?.[2]+")"}}
            />
        </div>
    );
}

export default BookshelfCovers;