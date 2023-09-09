import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";

import "./advanced.css";

function SearchAdvanced() {

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

    const handleSearch = async () => {
        var newSearch = "?";
        if (searchParams.term !== "") {
            newSearch = newSearch + "term=" + searchParams.term + "&";
        }
        if (searchParams.title !== "") {
            newSearch = newSearch + "title=" + searchParams.title + "&";
        }
        if (searchParams.author !== "") {
            newSearch = newSearch + "author=" + searchParams.author + "&";
        }
        if (searchParams.subject !== "") {
            newSearch = newSearch + "subject=" + searchParams.subject + "&";
        }
        if (searchParams.publisher !== "") {
            newSearch = newSearch + "publisher=" + searchParams.publisher + "&";
        }
        if (searchParams.isbn !== "") {
            newSearch = newSearch + "isbn=" + searchParams.isbn + "&";
        }
        if (searchParams.bookshelfName !== "") {
            newSearch = newSearch + "bookshelfname=" + searchParams.bookshelfName + "&";
        }
        if (searchParams.ownerName !== "") {
            newSearch = newSearch + "ownername=" + searchParams.ownerName + "&";
        }

        newSearch = newSearch + "resulttype=" + searchParams.resultType;

        navigate("/search/results" + newSearch);
    }

    return (
        <div className="searchAdvancedArea">
            <select
                value={searchParams.resultType}
                onChange={(e) => setSearchParams({ ...searchParams, resultType: e.target.value })}
            >
                <option value="book">Book</option>
                <option value="bookshelf">Bookshelf</option>
                <option value="all">All</option>
            </select>

            {searchParams.resultType === "book" || searchParams.resultType === "all" ?
                <>
                    <div className="label">Term:</div>
                    <input
                        type="text"
                        placeholder="Search term"
                        value={searchParams.term}
                        onChange={(e) => setSearchParams({ ...searchParams, term: e.target.value })}
                        spellCheck={false}
                    />
                    <div className="label">Title:</div>
                    <input
                        type="title"
                        placeholder="Search title"
                        value={searchParams.title}
                        onChange={(e) => setSearchParams({ ...searchParams, title: e.target.value })}
                        spellCheck={false}
                    />
                    <div className="label">Author:</div>
                    <input
                        type="author"
                        placeholder="Search author"
                        value={searchParams.author}
                        onChange={(e) => setSearchParams({ ...searchParams, author: e.target.value })}
                        spellCheck={false}
                    />
                    <div className="label">Subject:</div>
                    <input
                        type="subject"
                        placeholder="Search subject"
                        value={searchParams.subject}
                        onChange={(e) => setSearchParams({ ...searchParams, subject: e.target.value })}
                        spellCheck={false}
                    />
                    <div className="label">Publisher:</div>
                    <input
                        type="publisher"
                        placeholder="Search publisher"
                        value={searchParams.publisher}
                        onChange={(e) => setSearchParams({ ...searchParams, publisher: e.target.value })}
                        spellCheck={false}
                    />
                    <div className="label">Isbn:</div>
                    <input
                        type="isbn"
                        placeholder="Search isbn"
                        value={searchParams.isbn}
                        onChange={(e) => setSearchParams({ ...searchParams, isbn: e.target.value })}
                        spellCheck={false}
                    />
                </> : <></>
            }

            {searchParams.resultType === "bookshelf" || searchParams.resultType === "all" ?
                <>
                    <div className="label">Bookshelf Name:</div>
                    <input
                        type="bookshelfName"
                        placeholder="Search name"
                        value={searchParams.bookshelfName}
                        onChange={(e) => setSearchParams({ ...searchParams, bookshelfName: e.target.value })}
                        spellCheck={false}
                    />
                    <div className="label">Owner Name:</div>
                    <input
                        type="ownerName"
                        placeholder="Search owner"
                        value={searchParams.ownerName}
                        onChange={(e) => setSearchParams({ ...searchParams, ownerName: e.target.value })}
                        spellCheck={false}
                    />
                </> : <></>
            }

            <button onClick={handleSearch}>Search</button>
        </div>
    );
}

export default SearchAdvanced;





