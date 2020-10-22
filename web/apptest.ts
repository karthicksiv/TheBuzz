describe("Tests", function() {
    it("Test Modal", function(){
        // click the button for showing the add form
        $('#Navbar-add').click();
        return new Promise(function(resolve, reject) {
            setTimeout(function() {
                expect($("#NewEntryForm").attr("style").includes("display: block")).toEqual(true);
                $('NewEntryForm-Close').click();
                setTimeout(function() {
                    expect($("#NewEntryForm").attr("style").includes("display: none")).toEqual(true);
                    resolve();
                }, 100);
            }, 100);
        });
    });
});

var describe: any;
var it: any;
var expect: any; 
describe("Tests", function () {
  it("Test Modal", function () {
    // click the button for showing the add form
    $("#Navbar-add").click();
    return new Promise(function (resolve, reject) {
      setTimeout(function () {
        expect(
          $("#NewEntryForm").attr("style").includes("display: block")
        ).toEqual(true);
        $("NewEntryForm-Close").click();
        setTimeout(function () {
          expect(
            $("#NewEntryForm").attr("style").includes("display: none")
          ).toEqual(true);
          resolve();
        }, 100);
      }, 100);
    });
  });
});

var describe: any;
var it: any;
var expect: any;

// test view post
describe("Tests", function () {
  it("Test view edit", function () {
    // click the button for showing the add form
    return new Promise(function (resolve, reject) {
      setTimeout(function () {
        var button = $("table:first tr td:last-child button");
        button.click();
        expect(
          $("#ViewMessage").attr("style").includes("display: block")
        ).toEqual(true);
        $("#ViewMessage-comment").click();
        setTimeout(function () {
          expect(
            $("#ViewComment").attr("style").includes("display: block")
          ).toEqual(true);
          resolve();
        }, 100);
      }, 100);
    });
  });
});

// test edit post
describe("Tests", function () {
  it("Test view post", function () {
    // click the button for showing the add form
    $("#Navbar-add").click();
    return new Promise(function (resolve, reject) {
      setTimeout(function () {
        var button = $("table:first tr td::nth-child(4) button");
        button.click();
        expect(
          $("#EditEntryForm").attr("style").includes("display: block")
        ).toEqual(true);
        $("#EditEntryForm-Close").click();
        setTimeout(function () {
          expect(
            $("#EditEntryForm").attr("style").includes("display: none")
          ).toEqual(true);
          resolve();
        }, 100);
      }, 100);
    });
  });
});

// test my profile

describe("Tests", function () {
  it("Test Login Profile", function () {
    // click the button for showing the add form
    $("#Navbar-signin").click();
    return new Promise(function (resolve, reject) {
      setTimeout(function () {
        expect($("#SignIn").attr("style").includes("display: block")).toEqual(
          true
        );
        $("#SignIn-Close").click();
        setTimeout(function () {
          expect($("#SignIn").attr("style").includes("display: none")).toEqual(
            true
          );
          resolve();
        }, 100);
      }, 100);
    });
  });
});

// Test search by title

describe("Tests", function () {
  it("Test Search Title", function () {
    // click the button for showing the add form
    $("#Navbar-title").click();
    return new Promise(function (resolve, reject) {
      setTimeout(function () {
        expect($("#Navbar").attr("style").includes("display: block")).toEqual(
          false
        );
      }, 100);
    });
  });
});

describe("Tests", function () {
  it("Test Search Gen Search", function () {
    // click the button for showing the add form
    $("#Navbar-gensearch").click();
    return new Promise(function (resolve, reject) {
      setTimeout(function () {
        expect($("#Navbar").attr("style").includes("display: block")).toEqual(
          false
        );
      }, 100);
    });
  });
});

describe("Tests", function () {
  it("Test Search Message", function () {
    // click the button for showing the add form
    $("#Navbar-message").click();
    return new Promise(function (resolve, reject) {
      setTimeout(function () {
        expect($("#Navbar").attr("style").includes("display: block")).toEqual(
          false
        );
      }, 100);
    });
  });
});

describe("Tests", function () {
  it("Test Search Author", function () {
    // click the button for showing the add form
    $("#Navbar-author").click();
    return new Promise(function (resolve, reject) {
      setTimeout(function () {
        expect($("#Navbar").attr("style").includes("display: block")).toEqual(
          false
        );
      }, 100);
    });
  });
});

describe("Tests", function () {
  it("Test Search Date", function () {
    // click the button for showing the add form
    $("#Navbar-date").click();
    return new Promise(function (resolve, reject) {
      setTimeout(function () {
        expect($("#Navbar").attr("style").includes("display: block")).toEqual(
          false
        );
      }, 100);
    });
  });
});

describe("Tests", function () {
  it("Test Search Profile", function () {
    // click the button for showing the add form
    $("#Navbar-profile").click();
    return new Promise(function (resolve, reject) {
      setTimeout(function () {
        expect($("#Navbar").attr("style").includes("display: block")).toEqual(
          false
        );
      }, 100);
    });
  });
});




