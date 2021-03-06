/*
 * Copyright (C) 2014 Saravan Pantham
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jams.music.player.playlisteditor;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.jams.music.player.R;
import com.jams.music.player.helper.TypefaceHelper;
import com.jams.music.player.playlisteditor.PlaylistEditorAlbumsMultiselectAdapter.AsyncGetAlbumSongIds;
import com.jams.music.player.utils.Common;

public class AlbumsPickerFragment extends Fragment {

    private Context mContext;
    private Common mApp;
    public static Cursor cursor;
    public static ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mApp = (Common)getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_albums_music_library_editor, null);

        cursor = mApp.getDBAccessHelper().getAllUniqueAlbums("");
        listView = (ListView)rootView.findViewById(R.id.musicLibraryEditorAlbumsListView);
        listView.setFastScrollEnabled(true);
        listView.setAdapter(new PlaylistEditorAlbumsMultiselectAdapter(getActivity(), cursor));

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int which, long dbID) {
                CheckBox checkbox = (CheckBox)view.findViewById(R.id.albumCheckboxMusicLibraryEditor);
                checkbox.performClick();

				/* Since we've performed a software-click (checkbox.performClick()), all we have 
				 * to do now is determine the *new* state of the checkbox. If the checkbox is checked, 
				 * that means that the user tapped on it when it was unchecked, and we should add 
				 * the album's songs to the HashSet. If the checkbox is unchecked, that means the user 
				 * tapped on it when it was checked, so we should remove the album's songs from the 
				 * HashSet.
				 */
                if (checkbox.isChecked()) {
                    view.setBackgroundColor(0xCC0099CC);
                    AsyncGetAlbumSongIds task = new AsyncGetAlbumSongIds(mContext,
                            (String)view.getTag(R.string.album),
                            (String)view.getTag(R.string.artist));
                    task.execute(new String[] { "ADD" });
                } else {
                    view.setBackgroundColor(0x00000000);
                    AsyncGetAlbumSongIds task = new AsyncGetAlbumSongIds(mContext,
                            (String)view.getTag(R.string.album),
                            (String)view.getTag(R.string.artist));
                    task.execute(new String[] { "REMOVE" });
                }
            }
        });

        TextView instructions = (TextView)rootView.findViewById(R.id.albums_music_library_editor_instructions);
        instructions.setTypeface(TypefaceHelper.getTypeface(getActivity(), "RobotoCondensed-Light"));
        instructions.setPaintFlags(instructions.getPaintFlags() | Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);

        return rootView;
    }
}
