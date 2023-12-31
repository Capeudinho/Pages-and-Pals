import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";

import ButtonMode from "../../common/button/mode.js";
import alertContext from "../../context/alert.js";

import "./advanced.css";

function SearchAdvanced() {
    const {alert, setAlert} = useContext(alertContext);
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useState({
        term: "",
        title: "",
        author: "",
        subject: "",
        publisher: "",
        isbn: "",
        resultType: "book",
        bookshelfName: "",
        ownerName: "",
    });

    function handleSearch ()
    {
        var newSearch = "?";
        var flag = false;
        if (searchParams.term !== "") {
            flag = true;
            newSearch = newSearch + "term=" + searchParams.term + "&";
        }
        if (searchParams.title !== "") {
            flag = true;
            newSearch = newSearch + "title=" + searchParams.title + "&";
        }
        if (searchParams.author !== "") {
            flag = true;
            newSearch = newSearch + "author=" + searchParams.author + "&";
        }
        if (searchParams.subject !== "") {
            flag = true;
            newSearch = newSearch + "subject=" + searchParams.subject + "&";
        }
        if (searchParams.publisher !== "") {
            flag = true;
            newSearch = newSearch + "publisher=" + searchParams.publisher + "&";
        }
        if (searchParams.isbn !== "") {
            flag = true;
            newSearch = newSearch + "isbn=" + searchParams.isbn + "&";
        }
        if (searchParams.bookshelfName !== "" && (searchParams.resultType === "bookshelf" || searchParams.resultType === "all")) {
            flag = true;
            newSearch = newSearch + "bookshelfname=" + searchParams.bookshelfName + "&";
        }
        if (searchParams.ownerName !== "" && (searchParams.resultType === "bookshelf" || searchParams.resultType === "all")) {
            flag = true;
            newSearch = newSearch + "ownername=" + searchParams.ownerName + "&";
        }
        newSearch = newSearch + "resulttype=" + searchParams.resultType;
        if(flag === true){
            navigate("/search/results" + newSearch);
        }
        else{
            setAlert([{type: "warning", text: "Empty filters."}]);
        }
       
    }

    return (
        <div className="searchAdvancedArea">

            <ButtonMode
                modes={[{ text: "Book", type: "book" }, { text: "Bookshelf", type: "bookshelf" }, { text: "All", type: "all" }]}
                currentMode={searchParams.resultType}
                setMode={(newMode) => setSearchParams({ ...searchParams, resultType: newMode })}
                appearance={"switch"}
            />
            <div className="bigLabel">Book</div>
            <div className="label">Term</div>
            <input
                type="text"
                placeholder="Optional"
                value={searchParams.term}
                onChange={(e) => setSearchParams({ ...searchParams, term: e.target.value })}
                spellCheck={false}
            />
            <div className="label">Title</div>
            <input
                type="title"
                placeholder="Optional"
                value={searchParams.title}
                onChange={(e) => setSearchParams({ ...searchParams, title: e.target.value })}
                spellCheck={false}
            />
            <div className="label">Author</div>
            <input
                type="author"
                placeholder="Optional"
                value={searchParams.author}
                onChange={(e) => setSearchParams({ ...searchParams, author: e.target.value })}
                spellCheck={false}
            />
            <div className="label">Subject</div>
            <input
                type="subject"
                placeholder="Optional"
                value={searchParams.subject}
                onChange={(e) => setSearchParams({ ...searchParams, subject: e.target.value })}
                spellCheck={false}
            />
            <div className="label">Publisher</div>
            <input
                type="publisher"
                placeholder="Optional"
                value={searchParams.publisher}
                onChange={(e) => setSearchParams({ ...searchParams, publisher: e.target.value })}
                spellCheck={false}
            />
            <div className="label">ISBN</div>
            <input
                type="isbn"
                placeholder="Optional"
                value={searchParams.isbn}
                onChange={(e) => setSearchParams({ ...searchParams, isbn: e.target.value })}
                spellCheck={false}
            />
            {searchParams.resultType === "bookshelf" || searchParams.resultType === "all" ?
                <>
                    <div className="bigLabel">Bookshelf</div>
                    <div className="label">Name</div>
                    <input
                        type="bookshelfName"
                        placeholder="Optional"
                        value={searchParams.bookshelfName}
                        onChange={(e) => setSearchParams({ ...searchParams, bookshelfName: e.target.value })}
                        spellCheck={false}
                    />
                    <div className="label">Owner name</div>
                    <input
                        type="ownerName"
                        placeholder="Optional"
                        value={searchParams.ownerName}
                        onChange={(e) => setSearchParams({ ...searchParams, ownerName: e.target.value })}
                        spellCheck={false}
                    />
                </> : <></>
            }
            <button className="search" onClick={handleSearch}>Search</button>
        </div>
    );
}

export default SearchAdvanced;





